package com.hoangminh.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hoangminh.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.hoangminh.entity.Image;
import com.hoangminh.entity.Tour;
import com.hoangminh.repository.ImageRepository;
import com.hoangminh.repository.TourStartRepository;
import com.hoangminh.service.TourService;
import com.hoangminh.service.UserService;
import com.hoangminh.utilities.SessionUtilities;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	private TourService tourService;
	
	@Autowired
	private TourStartRepository tourStartRepository;

	@Autowired
	private ImageRepository imageRepository;
	
	@Autowired
	private UserService userService;

	@GetMapping("")
	ModelAndView index() {
		ModelAndView mdv = new ModelAndView("user/index");

		Page<TourDTO> tourPage = this.tourService.findAllTour(null, null, null, null, null, PageRequest.of(0, 6));

		List<TourDTO> tours = tourPage.getContent();
		
		mdv.addObject("tours", tours);
		
		
		return mdv;
	}

	@GetMapping("/tour/trong-nuoc")
	ModelAndView tourTrongNuoc(@RequestParam(value = "page", required = false, defaultValue = "10") Integer page,
			@RequestParam(value = "ten_tour", required = false) String ten_tour,
			@RequestParam(value = "gia_tour", required = false) Long gia_tour,
			@RequestParam(value = "ngay_khoi_hanh", required = false) String ngay_khoi_hanh) {
		ModelAndView mdv = new ModelAndView("user/tour1");
		Long gia_tour_from = null;
		Long gia_tour_to = null;
		if (gia_tour != null) {
			gia_tour_from = gia_tour == 0 ? null : (gia_tour == 1 ? 0 : (gia_tour == 2 ? 10000000l : 50000000l));

			gia_tour_to = gia_tour == 0 ? null : (gia_tour == 1 ? 10000000l : (gia_tour == 2 ? 50000000l : 500000000));
		}

		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

		Date ngay_khoi_hanh_value = null;
		try {
			ngay_khoi_hanh_value = ngay_khoi_hanh != null ? format.parse(ngay_khoi_hanh) : null;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Page<TourDTO> tourPage = this.tourService.findAllTour(ten_tour, gia_tour_from, gia_tour_to,
				ngay_khoi_hanh_value, 1, PageRequest.of(0, 10));

		List<TourDTO> tours = tourPage.getContent();

		mdv.addObject("tours", tours);
		return mdv;
	}

	@GetMapping("/tour/ngoai-nuoc")
	ModelAndView tourNgoaiNuoc(@RequestParam(value = "page", required = false, defaultValue = "10") Integer page,
			@RequestParam(value = "ten_tour", required = false) String ten_tour,
			@RequestParam(value = "gia_tour", required = false) Long gia_tour,
			@RequestParam(value = "ngay_khoi_hanh", required = false) String ngay_khoi_hanh) {

		Long gia_tour_from = null;
		Long gia_tour_to = null;
		if (gia_tour != null) {
			gia_tour_from = gia_tour == 0 ? null : (gia_tour == 1 ? 0 : (gia_tour == 2 ? 10000000l : 50000000l));

			gia_tour_to = gia_tour == 0 ? null : (gia_tour == 1 ? 10000000l : (gia_tour == 2 ? 50000000l : 500000000));
		}

		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

		Date ngay_khoi_hanh_value = null;
		try {
			ngay_khoi_hanh_value = ngay_khoi_hanh != null ? format.parse(ngay_khoi_hanh) : null;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ModelAndView mdv = new ModelAndView("user/tour2");

		Page<TourDTO> tourPage = this.tourService.findAllTour(ten_tour, gia_tour_from, gia_tour_to, ngay_khoi_hanh_value, 2,
				PageRequest.of(0, 10));

		List<TourDTO> tours = tourPage.getContent();

		mdv.addObject("tours", tours);
		return mdv;
	}

	@GetMapping("/tour/{id}")
	ModelAndView tourDetail(@PathVariable(name = "id", required = true) Long id) {
		ModelAndView mdv = new ModelAndView("user/tour-detail");

		TourDTO tour = this.tourService.findTourById(id);
		List<Image> imageList = this.imageRepository.findByTourId(id);

		
		List<TourStartDTO> listDate = this.tourStartRepository.getDateStartByTourId(id);
		
		mdv.addObject("tour", tour);
		mdv.addObject("listDate", listDate);
		mdv.addObject("imageList", imageList);

		return mdv;
	}
	
	@GetMapping("/login")
	ModelAndView login(HttpServletRequest request) {
		if(this.userService.checkLogin()) {
			return this.account();
		}
		ModelAndView mdv = new ModelAndView("user/login");
		
		return mdv;
	}
	
	@GetMapping("/register")
	ModelAndView register() {

		if(this.userService.checkLogin()) {
			return this.account();
		}

		ModelAndView mdv = new ModelAndView("user/register");
		
		return mdv;
	}
	
	
	@PostMapping("/login")
	ModelAndView loginAction(LoginDTO login,HttpServletRequest request) {

		
		String uriString = request.getRequestURI();
		
		ModelAndView mdv = new ModelAndView("user/account");
		
		if(!this.userService.login(login)) {
			ModelAndView mdvErr = new ModelAndView("user/login");
			mdvErr.addObject("err", "Sai thông tin đăng nhập");
			return mdvErr;
		}

		mdv.addObject("user", SessionUtilities.getUser());
		
		return mdv;
	}
	
	@PostMapping("/register")
	ModelAndView registerAction(RegisterDTO user) {


		ModelAndView mdv = new ModelAndView("user/login");
		
		if(!this.userService.register(user)) {
			ModelAndView mdvErr = new ModelAndView("user/register");
			mdvErr.addObject("err", "Đăng ký thất bại");
			return mdvErr;
		}
		mdv.addObject("message", "Đăng ký thành công vui lòng đăng nhập");
		return mdv;
	}

	@GetMapping("/logout")
	ModelAndView logout() {
		SessionUtilities.setUser(null);
		SessionUtilities.setUsername(null);
		return this.index();
	}
	
	@GetMapping("/account")
	ModelAndView account() {
		ModelAndView mdv = new ModelAndView("user/account");

		if(SessionUtilities.getUsername()==null) {
			ModelAndView loginView = new ModelAndView("user/login");
			return loginView;
		}
		
		mdv.addObject("user", SessionUtilities.getUser());

		return mdv;
	}

	@GetMapping("/changepassword")
	ModelAndView changePassword(ChangePasswordDTO changePasswordDTO) {

		ModelAndView mdv = new ModelAndView();
		if(!this.userService.checkLogin()) {
			mdv.setViewName("user/login");
			return mdv;
		}
		mdv.setViewName("user/changepassword");
		return mdv;

	}

	@PostMapping("/changePassword")
	ModelAndView changePasswordAction(ChangePasswordDTO changePasswordDTO) {

		if(changePasswordDTO.getNewPassword()!=null && changePasswordDTO.getOldPassword()!=null) {
			if(this.userService.changePassword(changePasswordDTO)) {
				ModelAndView accountView = this.account();
				accountView.addObject("message","Thay đổi mật khẩu thành công");
				return accountView;
			}
		}

		ModelAndView mdv = new ModelAndView("user/changepassword");
		mdv.addObject("err","Mật khẩu cũ không đúng");

		return mdv;
	}

	@PostMapping("/updateAccount")
	ModelAndView updateAccountAction(UpdateUserDTO updateUserDTO) {

		log.info("update account:{}",updateUserDTO);

		if(this.userService.updateUser(updateUserDTO)) {
			return this.account().addObject("message","Cập nhật thành công!");
		}else {
			return this.account().addObject("message","Có lỗi xảy ra!");
		}

	}

}
