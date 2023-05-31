/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trandpl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import trandpl.dbutil.DBConnection;
import trandpl.pojo.JobPojo;
import trandpl.pojo.ParticipantJobPojo;
import trandpl.pojo.ResultPojo;

/**
 *
 * @author Sir2
 */
public class JobDAO {
    public static int getNewJobId( ) throws SQLException
    {   
        Connection conn=DBConnection.getConnection();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("Select max(jobid) from jobs");
        
        rs.next();
        String strid=rs.getString(1);
        int jobId=101;
        if(strid!=null){
        String id=strid.substring(4);
        jobId=Integer.parseInt(id)+1;
        }
        return jobId;    
}
    
    public static boolean addNewJob(JobPojo job)throws SQLException{
         Connection conn=DBConnection.getConnection();
         PreparedStatement ps=conn.prepareStatement("insert into jobs values(?,?,?,?,?)");
         ps.setString(1, job.getJobId());
         ps.setString(2,job.getJobTitle());
         ps.setString(3, job.getHrId());
         ps.setString(4, job.getTags());
         ps.setInt(5, job.getStatus());
         return 1==ps.executeUpdate();
    }
    
    public static List<JobPojo> getAllActiveJobsByCurrentHr(String hrId)throws SQLException{
        
    Connection conn=DBConnection.getConnection();
    PreparedStatement ps=conn.prepareStatement("Select jobid,jobtitle,tags,status from jobs where hrid=? and status!=-1");
    ps.setString(1,hrId);
    ResultSet rs=ps.executeQuery();

    List <JobPojo> allJobsList=new ArrayList<>();
    while(rs.next()){
        JobPojo obj=new JobPojo();
        obj.setJobId(rs.getString(1));
        obj.setJobTitle(rs.getString(2));
        obj.setTags(rs.getString(3));
        obj.setStatus(rs.getInt(4));
        allJobsList.add(obj);
    }
    return allJobsList;
    }
    public static List<JobPojo> getAllOpenJobsByCurrentHr( String hrId ) throws SQLException  // Edit Job Dao Method  
    {

        Connection conn=DBConnection.getConnection();
PreparedStatement ps=conn.prepareStatement("select jobid , jobtitle , tags , status from jobs where hrid=? and status=1 order by jobid");
ps.setString(1, hrId);
ResultSet rs=ps.executeQuery();

        List<JobPojo>allJobList=new ArrayList<>();
        while(rs.next()){
JobPojo obj=new JobPojo();
obj.setJobId(rs.getString(1));
obj.setJobTitle(rs.getString(2));
obj.setTags(rs.getString(3));
obj.setStatus(rs.getInt(4));

allJobList.add(obj);
        }
        return allJobList;
    }

    
    public static boolean removeJobByJobId(String jobId)throws SQLException{
        Connection conn=DBConnection.getConnection();
        PreparedStatement ps=conn.prepareStatement("update jobs set status=-1 where jobid=?");
        ps.setString(1,jobId);
        return ps.executeUpdate()==1;
    }
    
    
    public static List<JobPojo>getAllEditableJobsByCurrentHr(String hrId)throws SQLException{
         Connection conn=DBConnection.getConnection();
PreparedStatement ps=conn.prepareStatement("Select jobid,jobtitle,tags,status from jobs where hrid=? and status=0");
ps.setString(1,hrId);
ResultSet rs=ps.executeQuery();
List<JobPojo>allJobsList=new ArrayList<>();
while(rs.next()){
JobPojo obj=new JobPojo();
obj.setJobId(rs.getString(1));
obj.setJobTitle(rs.getString(2));
obj.setTags(rs.getString(3));
obj.setStatus(rs.getInt(4));
allJobsList.add(obj);             
         }
         return allJobsList;
    }

    
    
    public static boolean editJobByJobId(JobPojo job)throws SQLException{
         Connection conn=DBConnection.getConnection();
PreparedStatement ps=conn.prepareStatement("update jobs set jobtitle=?, tags=? where jobid=?");
ps.setString(1,job.getJobTitle());
ps.setString(2, job.getTags());
ps.setString(3,job.getJobId());
         return 1==ps.executeUpdate();
    }

public static void setJobStatus(String jobId)throws SQLException
    {
PreparedStatement ps;
ps=DBConnection.getConnection().prepareStatement("update jobs set status=1 where jobId=?");
ps.setString(1,jobId);
ps.executeUpdate();
    }

public static List<ParticipantJobPojo> getAllAvailableJobs()throws SQLException
    {
        Statement st=DBConnection.getConnection().createStatement();
        ResultSet rs=st.executeQuery("select jobid,jobtitle,companyname,tags,jobs.hrid from jobs,hr where jobs.hrid=hr.hrid and status=1 order by jobid "); 
        List<ParticipantJobPojo> allOpenJobsList=new ArrayList<>();
         while(rs.next()){
             ParticipantJobPojo obj=new ParticipantJobPojo();
             obj.setJobId(rs.getString(1));
             obj.setJobTitle(rs.getString(2));
             obj.setCompanyName(rs.getString(3));
             obj.setTags(rs.getString(4));
             obj.setHrId(rs.getString(5));
             allOpenJobsList.add(obj);             
         }
         return allOpenJobsList;
    }



public static List<ParticipantJobPojo>getAllAppliedJobs(String particpantId)throws SQLException
    {
         Connection conn=DBConnection.getConnection();
PreparedStatement ps=conn.prepareStatement("Select jobs.jobid,jobs.jobtitle,companyname,tags from jobs,results,hr where jobs.jobid=results.jobid and jobs.hrid=hr.hrid and participantid=? and percentage=-1");
ps.setString(1, particpantId);
         List<ParticipantJobPojo>allAppliedJobsList=new ArrayList<>();
ResultSet rs=ps.executeQuery();
         while(rs.next())
         {
ParticipantJobPojo pj=new ParticipantJobPojo();
pj.setJobId(rs.getString(1));
pj.setJobTitle(rs.getString(2));
pj.setCompanyName(rs.getString(3));
pj.setTags(rs.getString(4));
allAppliedJobsList.add(pj);            
         }
         return allAppliedJobsList;

    }





}
