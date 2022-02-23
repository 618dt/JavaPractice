package com.lcg.reader.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class KaptchaController {
    /*引用Producer接口*/
    @Resource
    private Producer kaptchaProducer;//这里的属性名需要和配置的beanId一致;

    /*参数为原生的http请求和响应对象
     * 这样书写，把原生的对象放在参数列表中，则会在运行时由springIOC容器将当前的请求与
     * 响应对象动态的注入到对应的参数中*/
    @GetMapping("/verify_code")
    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置响应过期时间,0为立即过期
        response.setDateHeader("Expires", 0);
        /*设置缓存控制;因为每一次要求生成的验证码都是全新的；所以需要把浏览器的缓存都进行清空
         *no-store不存储  no-cache不缓存 must-revalidate必须重新校验*/
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        //处于兼容性考虑，平时一般用不到
        response.setHeader("Cache-Control", "post-check=0,pre-check=0");
        response.setHeader("Pragma", "no-cache");//以上三行都是设置浏览器不缓存任何图片数据
        response.setContentType("image/png");//返回类型为图片
        //生成验证码字符文本
        String verifyCode = kaptchaProducer.createText();
        //将验证码放到会话(Session)中
        request.getSession().setAttribute("kaptchaVerifyCode", verifyCode);
        //创建验证码图片,返回为二进制的图片
        BufferedImage image = kaptchaProducer.createImage(verifyCode);
        /*用getOutputStream方法向浏览器输出二进制文件(如图片等)*/
        ServletOutputStream out = response.getOutputStream();
        //将图片以png形式写入out输出流中
        ImageIO.write(image, "png", out);
        out.flush();//立即输出
        out.close();//关闭输出流
    }
}
