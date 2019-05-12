package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.yychat.model.Message;
import com.yychat.model.User;

public class StartServer {
    ServerSocket ss;
    Socket s;
    Message mess;
    String userName;
    String passWord;
    ObjectOutputStream oos;
    
    
    public static HashMap hmSocket=new HashMap<String,Socket>();//泛型，通用类
	public  StartServer(){
		try {
			ss=new ServerSocket(3456);//服务器端口监听3456
			System.out.println("服务器已经启动，监听3456端口...");
			while(true){//多线程问题
				s=ss.accept();//等待客户端建立连接
			System.out.println(s);//输出连接Socket对象
			
			//字节输入流包装对象输入流
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			
			User user=(User)ois.readObject();
			userName=user.getUserName();
			passWord=user.getPassWord();
			System.out.println(userName);
			System.out.println(passWord);
			
			//使用数据库来验证用户名和密码
			//1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			
			//2.建立连接
			String url1="jdbc:mysql://127.0.0.1:3306/yychat";
			String url="jdbc:mysql://127.0.0.1:3306/yychat?useUnicode=true&characterEncoding=UTF-8";
			String dbuser="root";
			String dbpass="";
			Connection conn=DriverManager.getConnection(url,dbuser,dbpass);
			Connection conn1=DriverManager.getConnection(url1,dbuser,dbpass);
			//3.建立一个preparedStatement
			String user_Login_Sql="select*from user where username=?and password=?";
			PreparedStatement ptmt=conn.prepareStatement(user_Login_Sql);
			ptmt.setString(1, userName);
			ptmt.setString(2, passWord);
			
			//4.执行preparedStatement
			ResultSet rs=ptmt.executeQuery();
			
			//判断结果集
			boolean loginSuccess=rs.next();
		    
			//Server端验证密码是否“123456”
			mess=new Message();	
			mess.setSender("Server");
			mess.setReceiver(user.getUserName());
			//if(user.getPassWord().equals("123456")){//不能用“==”，对象比较
			if(loginSuccess){//不能用“==”，对象比较
			    //激活新上线好友的图标。
				//1.向其他所有用户（比他先登录的）发送消息；
				mess.setMessageType(Message.message_NewOnLineFriend);
				mess.setSender("Server");
				mess.setContent(this.userName);//激活图标的用户名
				Set friendSet=hmSocket.keySet();
				Iterator it=friendSet.iterator();
				String friendName;
				while(it.hasNext()){
					friendName=(String)it.next();//取出每个好友名字
					mess.setReceiver(friendName);
					hmSocket.get(friendName);
					Socket s1=(Socket)hmSocket.get(friendName);
					sendMessage(s1,mess);
				}
				//消息传递,创建一个Message对象				
				mess.setMessageType(Message.message_LoginSuccess);//验证通过	
			    //保存每一个用户对应的Socket
				
				String friend_Relation_Sql="select slaveuser from relation where majoruser=? and relationtype='1'";
				ptmt=conn.prepareStatement(friend_Relation_Sql);
						ptmt.setString(1, userName);
				rs=ptmt.executeQuery();
				String friendString="";
				while(rs.next()){
					friendString=friendString+rs.getString("slaveuser")+" ";
				}
				mess.setContent(friendString);
				System.out.println(userName+"的全部好友： "+friendString);
			    
			}
			else{				
				mess.setMessageType(Message.message_LoginFailure);//验证不通过
			}
			
			sendMessage(s,mess);
			
			
			}
			
		} catch (IOException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}	
	}
	private void sendMessage(Socket s,Message mess) throws IOException {
		oos=new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(mess);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
