package com.oxchains.themis.user.service;

import com.oxchains.themis.common.constant.Const;
import com.oxchains.themis.common.util.DateUtil;
import com.oxchains.themis.user.domain.MailVO;
import com.oxchains.themis.user.domain.UResParam;
import com.sendgrid.*;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

/**
 * @author ccl
 * @time 2018-03-15 18:21
 * @name SendGridService
 * @desc:
 */
@Slf4j
@Service
public class SendGridService {

    private final String MAIL_REQUEST_ENDPOINT = "mail/send";

    private SendGrid sg;

    @Resource
    private UResParam uresParam;

    /**
     * 在类初始化后初始化发送邮件对象
     */
    @PostConstruct
    public void initSendGrid() throws FileNotFoundException {
        sg = new SendGrid(uresParam.getSendGridApiKey());
    }

    /*private SendGridService() throws FileNotFoundException, DocumentException {
        uresParam = new UResParam();
        uresParam.setFromAddr("noreply@themis.network");
        uresParam.setSendGridApiKey("1234567890");
        sg = new SendGrid(uresParam.getSendGridApiKey());
    }

    public static void main(String[] args) throws IOException, DocumentException {
        String fromAddress = "noreply@themis.network";
        String toAddress = "brandon-zhong8859@foxmail.com";
        String toAddress1 = "18296874638@163.com";
        String toAddress2 = "845123849@qq.com";
        String toAddress3 = "535904911@qq.com";
        String toAddress4 = "1582812410@qq.com";
        String presentDate = DateUtil.getPresentDate();
        SendGridService sendGridService = new SendGridService();
//        sendGridService.sendMail(fromAddress, toAddress, "mail test", "mail test  " + presentDate);
//        sendGridService.sendMail(fromAddress, toAddress1, "mail test", "mail test  " + presentDate);
//        sendGridService.sendMail(uresParam.getFromAddr(), toAddress2, "mail test", "mail test  " + presentDate);
//        sendGridService.sendMail(fromAddress, toAddress3, "mail test", "mail test  " + presentDate);
        File file = new File("E:\\var\\763293ce06bf1e684ef0ea3da43ae5008d8564b8.jpg");
        File file1 = new File("E:\\var\\20180827145820.jpg");
        File file2 = new File("E:\\var\\20180827153838.png");
//        sendGridService.sendMailWithAttachments(toAddress, "mail test", "<br/>  time -->" + presentDate, Const.MAIL_TYPE_HTML, file, file1, file2);
//        sendGridService.sendMailWithAttachments(toAddress1, "mail test", "mail test  " + presentDate, file, file1, file2);
        Map<String, String> content = new HashMap<>();
        content.put("time", presentDate);
        content.put("name", "brandon");
        content.put("message", "因为您、我有缘，所以才能共结一个圆。为进一步优化教育环境，加强家校互动，" +
                "共同促进学生的成长和进步，本着家校共育的精神，我校决定召开高一家长会，希望你在百忙中抽出时间，" +
                "拨冗光临，对孩子在家和在校的表现与各班主任进行深入的面对面交流，针对每个孩子的不同特点，" +
                "与老师共同商讨教育孩子的策略，最大程度的促进您孩子的进步。");
        sendGridService.sendHtmlMailWithAttachments(toAddress, "清华大学通知(Tsinghua University Notice)", content, "templates/emailTemplate.html", file, file1, file2);

    }*/

    /**
     * 发送带附件的HTML形式邮件
     *
     * @param toAddress 发送到的地址
     * @param subject   邮件的主题
     * @param content   邮件的内容参数,key为模板中标签的id值，value为该id标签中要填的值
     * @param filePath  邮件的html模板地址（该文件必须在resource目录下）
     * @param files     邮件的附件（可选，无附件也能发送成功）
     */
    public void sendHtmlMailWithAttachments(String toAddress, String subject, Map<String, String> content, String filePath, File... files) {
        String htmlStr = toHtmlStr(filePath, content);
        sendMailWithAttachments(uresParam.getFromAddr(), toAddress, subject, htmlStr, Const.MAIL_TYPE_HTML, files);
    }

    /**
     * 发送带附件的普通邮件
     *
     * @param toAddress 发送到的地址
     * @param subject   邮件的主题
     * @param content   邮件的内容
     * @param files     邮件的附件（可选，无附件也能发送成功）
     */
    public void sendTextMailWithAttachments(String toAddress, String subject, String content, File... files) {
        sendMailWithAttachments(uresParam.getFromAddr(), toAddress, subject, content, Const.MAIL_TYPE_TEXT, files);
    }


    /**
     * 发送带附件的邮件
     *
     * @param fromAddress 发送者的地址
     * @param toAddress   发送到的地址
     * @param subject     发送的主题
     * @param content     发送的内容
     * @param files       发送的附件，为数组形式
     */
    public void sendMailWithAttachments(String fromAddress, String toAddress, String subject, String content, String mailType, File... files) {
        try {
            //构建mail
            Mail mail = toMail(fromAddress, toAddress, subject, content, mailType);
            //构建attach并添加到mail中
            for (File file : files) {
                String fileName = file.getName();
                FileInputStream fileInputStream = new FileInputStream(file);
                Attachments attachments = new Attachments.Builder(fileName, fileInputStream).build();
                mail.addAttachments(attachments);
            }
            //发送mail
            sendMail(mail);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 构建mail
     *
     * @param from        发送人地址
     * @param to          发送到的地址
     * @param subject     邮件主题
     * @param content     邮件内容
     * @param messageType 邮件类型
     * @return 返回构建的邮件mail
     */
    private Mail toMail(String from, String to, String subject, String content, String messageType) {
        return new Mail(new Email(from), subject, new Email(to), new Content(messageType, content));
    }


    /**
     * 将map中的数据加入到模板中并返回模板的字符串形式
     *
     * @param filePath 模板所在的路径
     * @param content  要加入模板中的数据
     * @return 返回模板的字符串形式
     */
    public String toHtmlStr(String filePath, Map<String, String> content) {
        try {
            File file = ResourceUtils.getFile("classpath:" + filePath);
            Document htmlTemplate = new SAXReader().read(file);
            Element root = htmlTemplate.getRootElement();
            //为与数据中的key相同的id加入数据。
            Iterator<String> iterator = content.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                getNodes(root, "id", key).setText(content.get(key));
            }
            return htmlTemplate.asXML();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 方法描述：递归遍历子节点，根据属性名和属性值，找到对应属性名和属性值的那个子孙节点。
     *
     * @param node      要进行子节点遍历的节点
     * @param attrName  属性名
     * @param attrValue 属性值
     * @return 返回对应的节点或null
     */
    public Element getNodes(Element node, String attrName, String attrValue) {

        final List<Attribute> listAttr = node.attributes();// 当前节点的所有属性
        for (final Attribute attr : listAttr) {// 遍历当前节点的所有属性
            final String name = attr.getName();// 属性名称
            final String value = attr.getValue();// 属性的值
//            System.out.println("属性名称：" + name + "---->属性值：" + value);
            if (attrName.equals(name) && attrValue.equals(value)) {
                return node;
            }
        }
        // 递归遍历当前节点所有的子节点
        final List<Element> listElement = node.elements();// 所有一级子节点的list
        for (Element e : listElement) {// 遍历所有一级子节点
            Element temp = getNodes(e, attrName, attrValue);
            // 递归
            if (temp != null) {
                return temp;
            }
        }
        return null;
    }

    public void sendMail(String to, String subject, String content) throws IOException {
        sendMail(/*fromAddr*/uresParam.getFromAddr(), to, subject, content);
    }

    public void sendMail(String from, String to, String subject, String content) throws IOException {
//        SendGrid sg = new SendGrid(/*sendGridApiKey*/uresParam.getSendGridApiKey());
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        Mail mail = toMail(from, to, subject, content, Const.MAIL_TYPE_TEXT);
//        request.setBody("{\"personalizations\":[{\"to\":[{\"email\":\"" + to
//                + "\"}],\"subject\":\"" + subject + "\"}],\"from\":{\"email\":\"" + from + "\"}," +
//                "\"content\":[{\"type\":\"text/plain\",\"value\": \"" + content + "\"}]}");
        request.setBody(mail.build());
        Response response = sg.api(request);
        log.info(response.getStatusCode() + "");
        log.info(response.getBody());
        log.info(response.getHeaders() + "");
    }


    public void sendHtmlMail(String toAddr, String subject, String context) throws IOException {
        sendHtmlMail(/*fromAddr*/uresParam.getFromAddr(), toAddr, subject, context);
    }

    public void sendHtmlMail(String fromAddr, String toAddr, String subject, String context) throws IOException {
//        Email from = new Email(fromAddr);
//        Email  to = new Email(toAddr);
//        Content content = new Content(MAIL_TYPE_HTML,context);
//        Mail mail = new Mail(from, subject, to, content);
//        sendMail(mail);

        sendMail(fromAddr, toAddr, subject, context, Const.MAIL_TYPE_HTML);
    }


    public void sendMail(MailVO vo) throws IOException {
        Email from = new Email(vo.getFromAddress());
        from.setName(vo.getFromUser());

        Email to = new Email(vo.getToAddress());
        to.setName(vo.getToUser());
        Content content = new Content(vo.getType(), vo.getContent());
        Mail mail = new Mail(from, vo.getSubject(), to, content);

        sendMail(mail);
        log.info("邮件已经发送到：", vo.getToAddress());
    }

    public void sendMail(String fromAddr, String toAddr, String subject, String context, String type) throws IOException {
        Email from = new Email(fromAddr);
        Email to = new Email(toAddr);
        Content content = new Content(type, context);
        Mail mail = new Mail(from, subject, to, content);
//        mail.personalization.get(0).addSubstitution("-name-", "Example User");
//        mail.personalization.get(0).addSubstitution("-city-", "Denver");
//        mail.setTemplateId("13b8f94f-bcae-4ec6-b752-70d6cb59f932");
        sendMail(mail);
    }

    private void sendMail(Mail mail) throws IOException {
//        SendGrid sg = new SendGrid(/*sendGridApiKey*/uresParam.getSendGridApiKey());
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(MAIL_REQUEST_ENDPOINT);
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("" + response.getStatusCode());
            log.info(response.getBody());
            log.info(response.getHeaders().toString());
        } catch (IOException ex) {
            throw ex;
        }
    }


}
