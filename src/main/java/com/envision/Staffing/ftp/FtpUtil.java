package com.envision.Staffing.ftp;

import com.envision.Staffing.model.FtpDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {

	public static FtpDetails fieldExtraction(FtpDetails ftpDetails, boolean download) {

		String ftpUrl = ftpDetails.getFileUrl();		
		Pattern hostPattern = Pattern.compile("(ftp|sftp)://[^/]*/");
		Matcher hostMatcher = hostPattern.matcher(ftpUrl);
		if (hostMatcher.find()) {
			String match = hostMatcher.group(0);
			match = match.substring(0, match.length() - 1);
			match = match.replaceFirst("ftp://", "");
			ftpDetails.setHost(match);
		}

		Pattern dirPattern = Pattern.compile("[/][^:.]+/");
		Matcher dirMatcher = dirPattern.matcher(ftpUrl);
		if (dirMatcher.find()) {
			ftpDetails.setDirPath(dirMatcher.group(0));
		}

		if(download = false) {
			Pattern fileNamePattern = Pattern.compile("[/][^/:.]+[.][^:/.0-9]+");
			Matcher fileMatcher = fileNamePattern.matcher(ftpUrl);
			while (fileMatcher.find()) {
				ftpDetails.setFileName(fileMatcher.group(0));
			}
			ftpDetails.setFileName(ftpDetails.getFileName().substring(1));
			}
		return ftpDetails;
	}

	public static FTPClient connect(FtpDetails ftpDetails) {
		FTPClient ftp = new FTPClient();
		String host = ftpDetails.getHost();
		Integer port = 21; // ***
		String username = ftpDetails.getUsername();
		String password = ftpDetails.getPassword();

		try {
//			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

			int reply;
			ftp.connect(host, port);
			ftp.login(username, password);
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.setControlEncoding("GBK");
			ftp.setCharset(Charset.forName("GBK"));

			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ftp;
	}

	public static boolean downloadFile(String ftpUrl, String username, String password, String localDirPath) {
		FtpDetails ftpDetails = new FtpDetails(ftpUrl, username, password);
		ftpDetails = FtpUtil.fieldExtraction(ftpDetails, true);
		
		String dirPath = ftpDetails.getDirPath();
		String fileName = ftpDetails.getFileName();
		boolean flag = false;

		FTPClient ftp = connect(ftpDetails);

		if (ftp.isConnected()) {
			try {
				FileOutputStream fos = new FileOutputStream(localDirPath + fileName);
				ftp.retrieveFile(dirPath + fileName, fos);
				flag = true;

//				System.out.println("FTP File downloaded successfully");
				if (ftp.isConnected()) {
					ftp.logout();
					ftp.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public static boolean uploadFile(String ftpUrl, String username, String password, String localFilePath,
			String remoteFileName) {
		FtpDetails ftpDetails = new FtpDetails(ftpUrl, username, password);
		ftpDetails = FtpUtil.fieldExtraction(ftpDetails, false);
		
		String remoteDirPath = ftpDetails.getDirPath();
		String fileName = remoteFileName;

		boolean flag = false;

		FTPClient ftp = connect(ftpDetails);

		if (ftp.isConnected()) {
			try {
				FileInputStream input = new FileInputStream(new File(localFilePath));
				ftp.storeFile(remoteDirPath + fileName, input);
				input.close();
				flag = true;
//				System.out.println("Upload Successful");

				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
}
