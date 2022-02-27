package com.hexaphor.liveclass.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadFileUtil {
	
	@Value("${document.upload-directory}")
	String filePath;
	Path paths = Paths.get("src", "main", "resources", "static");

	Path utaklUniversityPath = Paths.get("filePath");
	
	
	public String assignmentFileSave(MultipartFile file,String assignmentId,String batchId) throws IOException {
		File Examfiles = new File(paths.toAbsolutePath() + File.separator + "/" + filePath + "/Assignment/");
		Examfiles.mkdirs();
		Examfiles.setWritable(true);
		Examfiles.setReadable(true);
		PDDocument document = PDDocument.load(file.getBytes());
		
		document.save(paths.toAbsolutePath() + File.separator + "/" + filePath + "/Assignment/" + assignmentId + "_"+batchId+ ".pdf");
		document.close();
		return "/" + filePath + "/Assignment/" + assignmentId + "_"+batchId+ ".pdf";
	}
	
	public String saveAnswerFile(MultipartFile file,String assignmentId,String studentId,String studentAssignmnetId) throws IOException {
		File Examfiles = new File(paths.toAbsolutePath() + File.separator + "/" + filePath + "/Answer/"+assignmentId+"/"+studentId+"/");
		Examfiles.mkdirs();
		Examfiles.setWritable(true);
		Examfiles.setReadable(true);
		PDDocument document = PDDocument.load(file.getBytes());
		
		document.save(paths.toAbsolutePath() + File.separator + "/" + filePath + "/Answer/"+assignmentId+"/"+studentId+"/" +studentAssignmnetId+ ".pdf");
		document.close();
		return "/" + filePath + "/Answer/"+assignmentId+"/"+studentId+"/" +studentAssignmnetId+ ".pdf";
	}

}
