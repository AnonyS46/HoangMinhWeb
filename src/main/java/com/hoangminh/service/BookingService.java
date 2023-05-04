package com.hoangminh.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hoangminh.dto.BookingDTO;
import com.hoangminh.entity.Booking;

public interface BookingService {

	Page<BookingDTO> findAllBooking(Integer trang_thai,String ten_tour,Pageable pageable);
	
	List<BookingDTO> findBookingByUserId(Long userId);
	
	Page<BookingDTO> findBookingByTourId(Long tour_Id,Pageable pageable);
	
	boolean addNewBooking(BookingDTO newBooking);
	
	boolean approveBooking(Long bookingId,Integer trang_thai);

	BookingDTO getBookingById(Long id);
}
