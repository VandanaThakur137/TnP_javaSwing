/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trandpl.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import trandpl.dbutil.DBConnection;
import trandpl.pojo.ParticipantPojo;

/**
 *
 * @author Sir2
 */
public class ParticipantDAO {
    
    public static int getNewParticipantId()throws SQLException{
        Connection conn=DBConnection.getConnection();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("Select max(pid) from  participants");
        int pId=101;
        rs.next();
        String strid=rs.getString(1);
        if(strid!=null){
        String id=strid.substring(2);
        pId=Integer.parseInt(id)+1;
        }
        return pId;
    }

   public static int addNewParticipant(ParticipantPojo pt)throws SQLException,FileNotFoundException{
       
       Connection conn=DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement("select userid from users where userid=?");
            ps.setString(1,pt.getUserId().toUpperCase().trim());
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return -1;   
            }
            ps=conn.prepareStatement("insert into users values(?,?,?,?,?,?)");
            ps.setString(1, pt.getUserId().toUpperCase());
            ps.setString(2, pt.getpId());
            ps.setString(3,pt.getName());
            ps.setString(4,pt.getPassword());
            ps.setString(5,pt.getType());
            ps.setString(6, "Y");
            int x=ps.executeUpdate();
            int y=0;
            if(x==1){
                ps=conn.prepareStatement("insert into participants values(?,?,?,?,?)");                
                ps.setString(1,pt.getpId());
                ps.setString(2,pt.getPhone());
                ps.setString(3,pt.getSkills());
                ps.setString(4,pt.getQualifications());
                FileInputStream resume = new FileInputStream(pt.getResume().getPath());
                ps.setBlob(5,resume);
                y=ps.executeUpdate();        

            }
            return y;


    }
   
   public static ParticipantPojo getParticipantById(String id)throws SQLException
    {
        Connection conn=DBConnection.getConnection();
PreparedStatement ps=conn.prepareStatement("select name,userid,phone from users,participants where pid=id and pid=?");    
ps.setString(1,id);
ResultSet rs=ps.executeQuery();
rs.next();
ParticipantPojo pt=new ParticipantPojo();
pt.setName(rs.getString(1));
pt.setUserId(rs.getString(2));
pt.setPhone(rs.getString(3));
        return pt;
    }

public static boolean updateResume(String pId,File file)throws SQLException,FileNotFoundException
    {
PreparedStatement ps;
ps=DBConnection.getConnection().prepareStatement("update participants set resume=? where pid=?");
FileInputStream resume = new FileInputStream(file.getPath());
ps.setBlob(1,resume);
ps.setString(2,pId);
        return 1==ps.executeUpdate();   
    }

public static boolean updateParticipantPassword(String pId,String pwd)throws SQLException{
        Connection conn=DBConnection.getConnection();
PreparedStatement ps=conn.prepareStatement("update users set password=? where id=?");
ps.setString(1, pwd);
ps.setString(2,pId);
        return ps.executeUpdate()==1;
    }


}
