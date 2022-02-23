package com.lcg.reader.controller.management;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lcg.reader.entity.Book;
import com.lcg.reader.entity.User;
import com.lcg.reader.service.BookService;
import com.lcg.reader.service.exception.BussinessException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/management/book")//规定后台页面以此url开头
public class MBookController {

    @Resource
    private BookService bookService;

    @GetMapping("/index.html")
    public ModelAndView showBook(HttpSession session) {
        User user = (User) session.getAttribute("loginAdmin");
        if (user == null) {//用户为空跳转到登录页
            return new ModelAndView("/management/login");
        }
        return new ModelAndView("/management/book");
    }

    //这里的地址要和book.ftl中设置的文件上传地址/management/book/upload一致
    /*MultipartFile用于接收文件,前面注解里的参数要和前面设置的上传参数一致
     * 原生的request用来获取发布路径*/
    @PostMapping("/upload")
    @ResponseBody
    public Map upload(@RequestParam("img") MultipartFile file, HttpServletRequest request) throws IOException {
        /*这行代码在运行时执行，即在web应用发布以后才执行，所以实际得到的路径是在
         * 工程发布的目录下(即out/artifacts/xxx_Web_expload)的路径,而不是webapp下的*/
        String uploadPath = request.getServletContext().getResource("/").getPath() + "/upload/";
        //生成文件名,利用SimpleDateFormat生成毫秒级文件名，以防止命名冲突
        String fileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        //文件扩展名，来自原始文件,所以将原始文件进行字符串的截取,从最后一次出现"."开始截取剩余的字符串
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //利用transferTo()(另存为方法)将上传的文件报存到我们指定的目录中;new File（）新建文件
        file.transferTo(new File(uploadPath + fileName + suffix));

        //按照wangEditor的要求组织返回结果
        Map result = new HashMap();
        result.put("errno", 0);//0,代表服务器处理正常
        //data要求是数组，所以新建一个数组,而上传的文件可以直接访问，所以返回的data填上其访问地址
        result.put("data", new String[]{"/upload/" + fileName + suffix});

        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public Map createBook(Book book) {
        Map result = new HashMap();
        try {
            book.setEvaluationQuantity(0);//补全评论数量
            book.setEvaluationScore(0f);//补全评分
            /*利用jsoup进行html的解析*/
            Document doc = Jsoup.parse(book.getDescription());
            //选中图书详情中所有的img标签,然后获取第一个
            Element img = doc.select("img").first();
            //获取当前元素指定的属性值,作为图片是在img标签中存在于src这个属性中的;
            String cover = img.attr("src");
            book.setCover(cover);//补全图书封面
            bookService.createBook(book);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }

    @GetMapping("/list")
    @ResponseBody
    public Map list(Integer page, Integer limit) {
        if (page == null) {//容错处理，设置为空时的默认值
            page = 1;
        }
        if (limit == null) {
            limit = 10;
        }
        //调用前面创建的分页方法获取图书分页对象
        IPage<Book> iPage = bookService.paging(null, null, page, limit);
        //按Layui的数据规范书写返回数据
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success");
        result.put("data", iPage.getRecords());//当前页面数据
        result.put("count", iPage.getTotal());//未分页时记录总数
        return result;
    }

    //获取数据进行回填
    @GetMapping("/id/{id}") //与前台保持一致的路径变量
    @ResponseBody
    public Map selectById(@PathVariable("id") Long bookId) {
        Book book = bookService.selectById(bookId);
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "success");
        result.put("data", book);//data在前端对应的是book数据，这里要和前面保持一致
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public Map updateBook(Book book) {
        Map result = new HashMap();
        try {
            /*这里注意不能直接对book进行更新，因为从前台传入过来的book对象中没有评分以及评论数量的信息
             * 需要根据前台传入的book通过id进行查询获取到原始的Book记录后才能进行更新操作*/
            Book rawBook = bookService.selectById(book.getBookId());//原始(未编辑前)的记录
            book.setEvaluationQuantity(rawBook.getEvaluationQuantity());
            book.setEvaluationScore(rawBook.getEvaluationScore());
            //获取封面
            Document doc = Jsoup.parse(book.getDescription());
            Element img = doc.select("img").first();
            String cover = img.attr("src");
            book.setCover(cover);//设置封面
            //并且修改要基于原始的rawBook，这样不容易出错，虽然基于book对象也行
            bookService.updateBook(book);
            result.put("code", "0");
            result.put("msg", "success");
            //result.put("data", book); 更新不需要返回数据，数据通过其他方法在前端显示
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }

    @GetMapping("/delete/{id}")
    @ResponseBody
    public Map deleteBook(@PathVariable("id") Long bookId) {
        Map result = new HashMap();
        try {
            bookService.deleteBook(bookId);
            result.put("code", "0");
            result.put("msg", "success");
        } catch (BussinessException e) {
            e.printStackTrace();
            result.put("code", e.getCode());
            result.put("msg", e.getMsg());
        }
        return result;
    }
}
