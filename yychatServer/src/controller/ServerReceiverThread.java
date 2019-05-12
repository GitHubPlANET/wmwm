package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{
	Socket s;
	HashMap hmSocket;
	Message mess;
	String sender;
	ObjectOutputStream oos;
	
	public ServerReceiverThread(Socket s,HashMap hmSocket){
		this.s=s;
		this.hmSocket=hmSocket;
	}
	public void run(){
			ObjectInputStream ois;
		
			while(true){
				try {
				ois=new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();
			System.out.println(mess.getSender()+"对"+mess.getReceiver()+"说："+mess.getContent());
			sender=mess.getSender();
		
			if(mess.getMessageType().equals(Message.message_Common)){
				Socket s1=(Socket)hmSocket.get(mess.getReceiver());
			    sendMessage(s1,mess);
					System.out.println("服务器转发了信息" + mess.getSender() + "对"+ mess.getReceiver() + "说：" + mess.getContent());
			
			}
              //第2步，返回在线好友信息到客户端
			if(mess.getMessageType().equals(Message.message_RequestOnlineFriend)){
				//首先要拿到在线好友信息
				Set friendSet=StartServer.hmSocket.keySet();
				Iterator it=friendSet.iterator();//迭代器，目的是对friendSet集合中的元素进行遍历
				String friendName;
				String friendString=" ";
				while(it.hasNext()){
					friendName=(String)it.next();//判断是否还有下一个好友
					if(!friendName.equals(mess.getSender()))//排除自己
					friendString=friendString+friendName+" ";
				}
				System.out.println("全部好友的名字："+friendString);
				
				//给mess赋值
			    mess.setContent(friendString);
			    mess.setReceiver(sender);
			    mess.setSender("Server");
			    mess.setMessageType(Message.message_OnlineFriend);
			    
			    //发送mess
			    Socket s1=(Socket)hmSocket.get(sender);
			    sendMessage(s1,mess);
				
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
			}
		
	}
	private void sendMessage(Socket s,Message mess) throws IOException {
		oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(mess);// 转达Massage
	}
}

