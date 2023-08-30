package com.example.Gab2.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.Gab2.DAO.UserRepository;
import com.example.Gab2.Model.User;

@Controller
public class Gab2ApplicationController {

	@Autowired
	UserRepository uRepo;

	@RequestMapping("/")
	String index(Model model) {
		List<User> list = uRepo.findAllByOrderByFullNameAsc();
		img(list);
		model.addAttribute("cards", list);
		return "index";
	}
	
	@GetMapping("/alphabet")
	public String filter(@RequestParam("letter") String letter, Model model) {
		List<User> list = uRepo.findByFullNameStartsWith(letter);
		if(list.isEmpty()) {
			model.addAttribute("message", "The List Is Empty!");
			return "index";
		}
		img(list);
		model.addAttribute("cards", list);
		return "index";
	}
	
	//image processing and filtering methods
	void img(List<User> list) {
		for (User user : list) {
			if(user.getPhoneNumber().contains(",")) {
				String[] phoneNumbers = user.getPhoneNumber().split(",");
				user.setPhoneNumber(phoneNumbers[0].trim());
				user.setPhoneNumber1(phoneNumbers[1].trim());
			}
			if (user.getImage() == null) {
				Path imagePath = Paths.get("C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/NullProfileImage.jpg");
				byte[] imageBytes = null;
				try {
					imageBytes = Files.readAllBytes(imagePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
				user.setImageStr(base64Image);
			}
			else {
				String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(user.getImage());
				user.setImageStr(base64Image);
			}
		}
	}
	
	//upload process
	@RequestMapping("/upload")
	String upload() {
		return "upload";
	}
	
	@PostMapping("/upload")
	String upload(@RequestParam("fullname") String fullName,
			@RequestParam("address") String address,
			@RequestParam("phone1") String phone1,
			@RequestParam("phone2") String phone2,
			@RequestParam("email") String email,
			@RequestParam("image") MultipartFile  image
			) {
		User user = new User();
		user.setFullName(fullName);
		user.setAddress(address);
		if(phone2.length() == 0) {
			user.setPhoneNumber(phone1);
		}
		else {
			user.setPhoneNumber(phone1 + ", " + phone2);
		}
		user.setMailId(email);
		user.setDateUpdated(LocalDateTime.now());
		if (!image.isEmpty()) {
            try {
                user.setImage(image.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		else {
			user.setImage(null);
		}
		uRepo.save(user);
		return "upload";
	}
}