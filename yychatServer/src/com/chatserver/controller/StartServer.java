package com.chatserver.controller;

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

import controller.ServerReceiverThread;

public class StartServer {
    ServerSocket ss;
    Socket s;
    Message mess;
    String userName;
    String passWord;
    ObjectOutputStream oos;
    
    
    public static HashMap hmSocket=new HashMap<String,Socket>();//���ͣ�ͨ����
	public  StartServer(){
		try {
			ss=new ServerSocket(3456);//�������˿ڼ���3456
			System.out.println("�������Ѿ�����������3456�˿�...");
			while(true){//���߳�����
				s=ss.accept();//�ȴ��ͻ��˽�������
			System.out.println(s);//�������Socket����
			
			//�ֽ���������װ����������
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			
			User user=(User)ois.readObject();
			userName=user.getUserName();
			passWord=user.getPassWord();
			System.out.println(userName);
			System.out.println(passWord);
			
			//ʹ�����ݿ�����֤�û���������
			//1.������������
			Class.forName("com.mysql.jdbc.Driver");
			
			//2.��������
			String url1="jdbc:mysql://127.0.0.1:3306/yychat";
			String url="jdbc:mysql://127.0.0.1:3306/yychat?useUnicode=true&characterEncoding=UTF-8";
			String dbuser="root";
			String dbpass="";
			Connection conn=DriverManager.getConnection(url,dbuser,dbpass);
			Connection conn1=DriverManager.getConnection(url1,dbuser,dbpass);
			//3.����һ��preparedStatement
			String user_Login_Sql="select*from user where username=? and password=?";
			PreparedStatement ptmt=conn.prepareStatement(user_Login_Sql);
			ptmt.setString(1, userName);
			ptmt.setString(2, passWord);
			
			//4.ִ��preparedStatement
			ResultSet rs=ptmt.executeQuery();
			
			//�жϽ����
			boolean loginSuccess=rs.next();
			System.out.println(loginSuccess);
		    
			//Server����֤�����Ƿ�123456��
			mess=new Message();	
			mess.setSender("Server");
			mess.setReceiver(user.getUserName());
			//if(user.getPassWord().equals("123456")){//�����á�==��������Ƚ�
			if(loginSuccess){
				//��Ϣ����,����һ��Message����				
				mess.setMessageType(Message.message_LoginSuccess);//��֤ͨ��
				 //����ÿһ���û���Ӧ��Socket
			    hmSocket.put(userName, s);
			    //��ν��տͻ���������Ϣ
			    new ServerReceiverThread(s,hmSocket).start();
				//�����ݿ�relation���ж�ȡ������Ϣ�����º����б�
				//1.���������������ݳ���
				String friend_Relation_Sql="select slaveuser from relation where majoruser=? and relationtype='1'";
				ptmt=conn.prepareStatement(friend_Relation_Sql);
				ptmt.setString(1, userName);
				rs=ptmt.executeQuery();
				String friendString="";
				while(rs.next()){//�ƶ�������е�ָ�룬һ������ȡ�����ѵ�����
					//rs.getString(1);
					friendString=friendString+rs.getString("slaveuser")+" ";
					
				}
				mess.setContent(friendString);
				System.out.println(userName+"ȫ�����ѣ�"+friendString);
			}
			else{
				mess.setMessageType(Message.message_LoginFailure);//��֤��ͨ��
			}
			sendMessage(s,mess);
			
			System.out.println(loginSuccess);
			if(loginSuccess){//�����á�==��������Ƚ�	
			    //���������ߺ��ѵ�ͼ�ꡣ
				//1.�����������û��������ȵ�¼�ģ�������Ϣ��
				mess.setMessageType(Message.message_NewOnLineFriend);
				mess.setSender("Server");
				mess.setContent(this.userName);//����ͼ����û���
				Set friendSet=hmSocket.keySet();
				Iterator it=friendSet.iterator();
				String friendName;
				while(it.hasNext()){
					friendName=(String)it.next();//ȡ��ÿ����������
					mess.setReceiver(friendName);
					hmSocket.get(friendName);
					Socket s1=(Socket)hmSocket.get(friendName);
					sendMessage(s1,mess);
				}
			}	
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