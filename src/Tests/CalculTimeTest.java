package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Delivery;
import model.Journey;
import model.Round;
import model.RoundSet;

class CalculTimeTest {

	@Test
	void calculTimeTest() {

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:SS");
		try {
			Date startTime = df.parse("10:10:0:000");

			// Creation of deliveries
			ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
			deliveries.add(new Delivery(null)); // warehouse
			long Adate1Expected = startTime.getTime();
			long Ddate1Expected = startTime.getTime();
			deliveries.add(new Delivery(60, null)); // 1
			long Adate2Expected = df.parse("10:10:01:200").getTime();
			long Ddate2Expected = df.parse("10:11:01:200").getTime();
			deliveries.add(new Delivery(120, null)); // 2
			long Adate3Expected = df.parse("10:11:03:600").getTime();
			long Ddate3Expected = df.parse("10:13:03:600").getTime();
			deliveries.add(new Delivery(60, null)); // 3
			long Adate4Expected = df.parse("10:13:04:800").getTime();
			long Ddate4Expected = df.parse("10:14:04:800").getTime();

			// Creation of deliveries 2
			ArrayList<Delivery> deliveries2 = new ArrayList<Delivery>();
			deliveries2.add(new Delivery(0, null)); // warehouse
			long Adate1Expected2 = startTime.getTime();
			long Ddate1Expected2 = startTime.getTime();
			deliveries2.add(new Delivery(60, null)); // 1
			long Adate2Expected2 = df.parse("10:10:02:400").getTime();
			long Ddate2Expected2 = df.parse("10:11:02:400").getTime();
			deliveries2.add(new Delivery(300, null)); // 2
			long Adate3Expected2 = df.parse("10:11:07:200").getTime();
			long Ddate3Expected2 = df.parse("10:16:07:200").getTime();


			// Creation of journey
			List<Journey> journeys = new ArrayList<Journey>();
			journeys.add(new Journey(null, null, null, 5)); // w -> 1
			journeys.add(new Journey(null, null, null, 10)); // 1 -> 2
			journeys.add(new Journey(null, null, null, 5)); // 2 -> 3
			journeys.add(new Journey(null, null, null, 10)); // 3 -> w

			// Creation of journey 2
			List<Journey> journeys2 = new ArrayList<Journey>();
			journeys2.add(new Journey(null, null, null, 10)); // w -> 1
			journeys2.add(new Journey(null, null, null, 20)); // 1 -> 2
			journeys2.add(new Journey(null, null, null, 10)); // 2 -> w

			// Creation of Round
			Round round = new Round();
			round.setDeliveries(deliveries);
			round.setJourneys(journeys);

			// Creation of Round2
			Round round2 = new Round();
			round2.setDeliveries(deliveries2);
			round2.setJourneys(journeys2);

			// Creation of Roundset
			RoundSet roundSet = new RoundSet();
			roundSet.getRounds().add(round);
			roundSet.getRounds().add(round2);
			roundSet.setDepartureTime(startTime);

			roundSet.calculTime();

			assertEquals(Adate1Expected+round.getDuration(), deliveries.get(0).getArrivalTime().getTime());
			assertEquals(Adate2Expected, deliveries.get(1).getArrivalTime().getTime());
			assertEquals(Adate3Expected, deliveries.get(2).getArrivalTime().getTime());
			assertEquals(Adate4Expected, deliveries.get(3).getArrivalTime().getTime());
			assertEquals(Ddate1Expected, deliveries.get(0).getDepartureTime().getTime());
			assertEquals(Ddate2Expected, deliveries.get(1).getDepartureTime().getTime());
			assertEquals(Ddate3Expected, deliveries.get(2).getDepartureTime().getTime());
			assertEquals(Ddate4Expected, deliveries.get(3).getDepartureTime().getTime());
			assertEquals(Adate1Expected2+round2.getDuration(), deliveries2.get(0).getArrivalTime().getTime());
			assertEquals(Adate2Expected2, deliveries2.get(1).getArrivalTime().getTime());
			assertEquals(Adate3Expected2, deliveries2.get(2).getArrivalTime().getTime());
			assertEquals(Ddate1Expected2, deliveries2.get(0).getDepartureTime().getTime());
			assertEquals(Ddate2Expected2, deliveries2.get(1).getDepartureTime().getTime());
			assertEquals(Ddate3Expected2, deliveries2.get(2).getDepartureTime().getTime());
			assertEquals(247200, round.getDuration());
			assertEquals(369600, round2.getDuration());
			assertEquals(369600, roundSet.getDuration());

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
