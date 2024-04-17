package com.keep.changes.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String randomUuid = UUID.randomUUID().toString();
		String newFileName = randomUuid.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));

		String filePath = path + File.separator + newFileName;

		System.out.println("path = " + path);
		System.out.println("filepath = " + filePath);
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		System.out.println("done");
		Files.copy(file.getInputStream(), Paths.get(filePath));
		return newFileName;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullPath = path + File.separator + fileName;
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}

	public boolean deleleFile(String path, String fileName) throws IOException {

		boolean deleted = false;

		String fullPath = path + File.separator + fileName;
		Path filePath = Paths.get(fullPath);
		try {
			Files.delete(filePath);
			deleted = true;
		} catch (IOException e) {
			throw new IOException("Could not delete File", e);
		}
		return deleted;

	}
}
