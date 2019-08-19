package com.tedu.base.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevCommitList;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import com.tedu.base.commitLog.model.CommitFile;
import com.tedu.base.commitLog.model.CommitLog;


public class JGitUtils {
	//private final String GIT_PATH = "D:\\gl\\programs\\gitProgram\\base-code\\.git" ;
	//private final String REMOTE_URL = "git@codehub.devcloud.huaweicloud.com:b94fd4e127ed4288a784cf5a41845633/base-code.git";
	private final String REMOTE_URL = "D:\\gl\\programs\\clone\\base-code\\.git";
	private Repository repository ;
	private static final Logger log = Logger.getLogger(JGitUtils.class);
	
	public Map<String, List> getLog(Date tableLastTime){
		Map<String, List> logMap = null;
		JGitUtils g = new JGitUtils();
		
		//取日志
		Git git = g.gitConnect();
		try {
			logMap  = g.getGitLog(git,tableLastTime);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		//关闭
		g.closeGit(git);
		
		return logMap;
	}
	
/*	public static void main(String[] args) {
		JGitUtils g = new JGitUtils();
		
		//取日志
		Git git = g.gitConnect();
		try {
			g.getGitLog(git);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//关闭
		g.closeGit(git);
	}*/

	/**
	 * 
	 * @Description: git连接
	 * @author: gaolu
	 * @date: 2017年11月2日 下午3:30:00  
	 * @param: 
	 * @return: Git
	 */
	public Git gitConnect(){
		Git git = null;
        File localPath;
		try {
			localPath = File.createTempFile("TestGitRepository", "");
			localPath.delete();
			Git.cloneRepository()
			.setURI(REMOTE_URL)
			.setDirectory(localPath)
			.call();
			git = Git.open( localPath);
			repository = git.getRepository();  
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return git;
	}
	
	/**
	 * 
	 * @Description: 
	 * @author: gaolu
	 * @date: 2017年11月2日 下午3:34:24  
	 * @param:      
	 * @return: void
	 */
	public Map<String, List> getGitLog(Git git,Date tableLastTime) throws Exception{
		Map<String, List> logMap = new HashMap<>();
		List<CommitLog> logList = new ArrayList<>();
		List<CommitFile> reportList = new ArrayList<>();
		int index = 0;
		RevCommitList<RevCommit> rList = new RevCommitList<>();
        //for(RevCommit revCommit : git.log().setMaxCount(2).call()){
    	for(RevCommit revCommit : git.log().call()){
    		Date commitTime = DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss",DateUtils.stampToDate(String.valueOf(revCommit.getCommitTime())));
    		
    		//提交时间在git日志表中最后一条记录的提交时间之前不进行存储（去重）
    		//if(commitTime.getTime() <= tableLastTime.getTime()){
            	//数据赋值并放入list中
            	CommitLog cLog = new CommitLog();
            	cLog.setSha1(revCommit.getName());
            	cLog.setBranch("");
            	cLog.setAuthor(revCommit.getCommitterIdent().getName());
            	cLog.setDate(commitTime);
            	String message = revCommit.getFullMessage();
                cLog.setMessage(message);
                cLog.setJobId(0);
                if (message.contains("#")) {
                	message = getIssueId(message);
                }else{
                	message = "";
                }
            	cLog.setIssueId(message);
                logList.add(cLog);
                
                //循环的第一个不用进行比较
                if (index!=0) {
                	AbstractTreeIterator newTree = prepareTreeParser(rList.get(index-1));  
                	AbstractTreeIterator oldTree = prepareTreeParser(revCommit);
                	List<DiffEntry> diff = (List<DiffEntry>) git.diff().setOldTree(oldTree).setNewTree(newTree).setShowNameAndStatusOnly(true).call();

                	ByteArrayOutputStream out = new ByteArrayOutputStream();    
                    DiffFormatter df = new DiffFormatter(out);   
                    //设置比较器为忽略空白字符对比（Ignores all whitespace）  
                    df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);  
                    df.setRepository(git.getRepository());   
                    //每一个diffEntry都是第个文件版本之间的变动差异  
                    for (DiffEntry diffEntry : diff) {   
                        //获取文件差异位置，从而统计差异的行数，如增加行数，减少行数  
                        FileHeader fileHeader = df.toFileHeader(diffEntry);  
                        List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();  
                        int addSize = 0;  
                        int subSize = 0;  
                        for(HunkHeader hunkHeader:hunks){  
                            EditList editList = hunkHeader.toEditList();  
                            for(Edit edit : editList){  
                                subSize += edit.getEndA()-edit.getBeginA();  
                                addSize += edit.getEndB()-edit.getBeginB();  
                            }  
                        }
                    	
                        //数据赋值并放到list中
                        CommitFile cFile = new CommitFile();
                        String extension = "";
                        cFile.setCommitSha1(rList.get(index-1).getName());
                    	if ("ADD".equals(diffEntry.getChangeType().toString())) {
                    		cFile.setPath(diffEntry.getNewPath());
                    		extension = diffEntry.getNewPath().toString();
    					}else{
    						cFile.setPath(diffEntry.getOldPath());
    						extension = diffEntry.getOldPath().toString();
    					}
                    	extension = extension.substring(extension.lastIndexOf(".")+1, extension.length());
                    	cFile.setStatus(diffEntry.getChangeType().toString());
                    	cFile.setExtension(extension);
                    	cFile.setAddLines(addSize);
                    	cFile.setRemoveLines(subSize);
                    	reportList.add(cFile);
                        out.reset();    
                   }   
    			}
                index += 1;
                rList.add(revCommit);
                //分支、工作项编号、创建时间   			
    		}
        //}
        logMap.put("commitlog", logList);
        logMap.put("report", reportList);
        return logMap;
	}
	
	/**
	 * 
	 * @Description: 获取提交记录树节点
	 * @author: gaolu
	 * @date: 2017年12月13日 下午6:49:19  
	 * @param:      
	 * @return: AbstractTreeIterator
	 */
    public AbstractTreeIterator prepareTreeParser(RevCommit commit){  
        try (RevWalk walk = new RevWalk(repository)) {  
            RevTree tree = walk.parseTree(commit.getTree().getId());  
  
            CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();  
            try (ObjectReader oldReader = repository.newObjectReader()) {  
                oldTreeParser.reset(oldReader, tree.getId());  
            }  
            walk.dispose();  
  
            return oldTreeParser;  
    }catch (Exception e) {  
        // TODO: handle exception  
    }  
        return null;  
    }  

    /**
     * 
     * @Description: 关闭git流
     * @author: gaolu
     * @date: 2017年12月13日 下午6:49:08  
     * @param:      
     * @return: void
     */
	public void closeGit(Git git){
		if (git != null) {
			git.close();
		}
	}
	
	/**
	 * 
	 * @Description: 根据"#+数字"的格式截取工作项id
	 * @author: gaolu
	 * @date: 2017年12月13日 下午6:48:35  
	 * @param:      
	 * @return: String
	 */
	private String getIssueId(String msg){
		int length = msg.length();
		int startidx = msg.indexOf("#");
		int endidx = length;
		for(int i = startidx+1 ; i < length; i++){
			if (!Character.isDigit(msg.charAt(i))) {
				endidx = i;
				break;
			}
		}
		msg = msg.substring(startidx+1, endidx);
		return msg;
	}
	
	
	/*    public static void main(String[] args) throws IOException
    {
    	Runtime rt = Runtime.getRuntime();
    	String[] commands = {"D:\\gl\\programTools\\Git\\Git\\git-bash.exe","$ git log --stat --oneline >> 1.txt"};
    	Process proc = rt.exec(commands);
    	BufferedReader stdInput = new BufferedReader(new 
    			InputStreamReader(proc.getInputStream()));
    	
    	BufferedReader stdError = new BufferedReader(new 
    			InputStreamReader(proc.getErrorStream()));
    	
    	// read the output from the command
    	System.out.println("Here is the standard output of the command:\n");
    	String s = null;
    	while ((s = stdInput.readLine()) != null) {
    		System.out.println(s);
    	}
    	
    	// read any errors from the attempted command
    	System.out.println("Here is the standard error of the command (if any):\n");
    	while ((s = stdError.readLine()) != null) {
    		System.out.println(s);
    	}


    }*/
}
