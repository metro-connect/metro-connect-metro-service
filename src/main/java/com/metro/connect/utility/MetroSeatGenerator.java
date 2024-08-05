package com.metro.connect.utility;

import java.util.ArrayList;
import java.util.List;

import com.metro.connect.entity.MetroSeat;

public class MetroSeatGenerator {

	public static List<MetroSeat> generateMetroSeat(int totalCoaches, int seatsPerCoach, int metroId) {
		List<MetroSeat> metroSeats = new ArrayList<>();
		int startAscii = 65; // ASCII value of 'A'

		for (int i = 0; i < totalCoaches; i++) {
			char coachLabel = (char) (startAscii + i);

			for (int j = 1; j <= seatsPerCoach; j++) {
				String seatLabel = coachLabel + String.valueOf(j);

				MetroSeat seat = new MetroSeat();
				seat.setSeatNo(seatLabel);
				seat.setMetroId(metroId);

				metroSeats.add(seat);
			}
		}

		return metroSeats;
	}

}
