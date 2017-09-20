package cn.edu.xiyou.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;


public class UploadUtil {
	
	public cn.edu.xiyou.sys.entity.File upload(HttpServletRequest request, MultipartFile file)
			throws Exception {
		System.out.println("开始");
		try {  
            //拿到输出流，同时重命名上传的文件  
			String logoRealPathDir = request.getSession().getServletContext()
	                .getRealPath("/upload");
			System.out.println("上传地址为 ：" + logoRealPathDir);
			 File folder = new File(logoRealPathDir);
             if(!folder.exists())folder.mkdirs();
            FileOutputStream os = new FileOutputStream(logoRealPathDir + "/" + file.getOriginalFilename());  
            //拿到上传文件的输入流  
            FileInputStream in = (FileInputStream) file.getInputStream();  
              
            //以写字节的方式写文件  
            int b = 0;  
            while((b=in.read()) != -1){   
                os.write(b);  
            }  
            os.flush();  
            os.close();  
            in.close();  
              System.out.println("结束");
              
        } catch (Exception e) {  
            e.printStackTrace();  
            System.out.println("上传出错");  
        }  
				return null;
			}
	
	}
