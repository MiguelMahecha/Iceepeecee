package iceepeecee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IceepeeceeTest {
    private Iceepeecee simulator;
    private int[][][] testFlights;
    private int[][][] testIslands;

    @BeforeEach
    void setup() {
        testFlights = new int[][][]{
                {{0, 30, 20}, {78, 70, 5}},
                {{55, 0, 20}, {70, 60, 10}}
        };

        testIslands = new int[][][]{
                {{20, 30}, {50, 50}, {10, 50}},
                {{40, 20}, {60, 10}, {75, 20}, {60, 30}},
                {{45, 60}, {55, 55}, {60, 60}, {55, 65}}
        };
        simulator = new Iceepeecee(1000, 1000);
        simulator.setHeadless(true);
    }

//    Adding an Island

    @Test
    void shouldAddValidIsland() {
        try {
            simulator.addIsland("blue", testIslands[0]);
            assertNotNull(simulator.loadIsland("blue"));
        } catch (IceepeeceeException e) {
            fail("AddIsland threw and exception");
        }
    }

    @Test
    void shouldNotAddInvalidIsland() {
        try {
            simulator.addIsland("red", new int[][]{{-3, 4}, {5, 3}, {1010, 6}});
            fail("Did not throw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.INVALID_ISLAND);
        }
    }

    @Test
    void shouldNotAddRepeatedIslandColor() {
        try {
            simulator.addIsland("red", testIslands[0]);
            simulator.addIsland("red", testIslands[1]);
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.ISLAND_ALREADY_EXISTS);
        }
    }

    //    Adding a Flight
    @Test
    void shouldAddValidFlight() {
        try {
            simulator.addFlight("yellow", testFlights[0][0], testFlights[0][1]);
            assertNotNull(simulator.loadFlight("yellow"));
        } catch (IceepeeceeException e) {
            fail("addFlight threw exception");
        }
    }

    @Test
    void shouldNotAddInvalidFlight() {
        try {
            simulator.addFlight("yellow", testFlights[0][0], new int[]{20, 1040, 4});
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.INVALID_FLIGHT);
        }
    }

    @Test
    void shouldNotAddFlightThatAlreadyExists() {
        try {
            simulator.addFlight("red", testFlights[0][0], testFlights[0][1]);
            simulator.addFlight("red", testFlights[1][0], testFlights[1][1]);
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.FLIGHT_ALREADY_EXISTS);
        }
    }

//    PHOTOGRAPH TESTS

    @Test
    void shouldTakePhotographThetaInt() {
        try {
            simulator.photograph(50);
        } catch (IceepeeceeException e) {
            fail("Photograph int threw exception");
        }
    }

    @Test
    void shouldTakePhotographThetaFloat() {
        try {
            simulator.photograph(50.0f);
        } catch (IceepeeceeException e) {
            fail("Photograph float threw exception");
        }
    }

    @Test
    void shouldTakePhotographThetaIntGivenFlight() {
        try {
            simulator.addFlight("red", testFlights[0][0], testFlights[0][1]);
            simulator.photograph("red", 50);
        } catch (IceepeeceeException e) {
            fail("Photograph float threw exception");
        }
    }

    @Test
    void shouldNotTakePhotographInvalidAngle() {
        try {
            simulator.photograph(190.0f);
            simulator.photograph(-5.0f);
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.INVALID_ANGLE);
        }
    }

    @Test
    void shouldNotTakePhotographNonExistentFlight() {
        try {
            simulator.photograph("red", 50);
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.FLIGHT_NOT_FOUND);
        }
    }

    //    DELETE ISLANDS
    @Test
    void shouldDeleteIsland() {
        try {
            simulator.addIsland("red", testIslands[0]);
            assertNotNull(simulator.loadIsland("red"));
            simulator.delIsland("red");
            assertNull(simulator.loadIsland("red"));
        } catch (IceepeeceeException e) {
            fail("Threw exception");
        }
    }

    @Test
    void shouldNotDeleteIslandThatDoesntExists() {
        try {
            simulator.delIsland("red");
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.ISLAND_NOT_FOUND);
        }
    }

    @Test
    void shouldDeleteFlight() {
        try {
            simulator.addFlight("red", testFlights[0][1], testFlights[0][1]);
            assertNotNull(simulator.loadFlight("red"));
            simulator.delFlight("red");
            assertNull(simulator.loadFlight("red"));
        } catch (IceepeeceeException e) {
            fail("Threw exception");
        }
    }

    @Test
    void shouldNotDeleteFlightThatDoesNotExist() {
        try {
            simulator.delFlight("red");
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.FLIGHT_NOT_FOUND);
        }
    }

    //    QUERIES
    @Test
    void shouldGetIslandLocation() {
        try {
            simulator.addIsland("red", testIslands[0]);
            assertEquals(simulator.islandLocation("red"), testIslands[0]);
        } catch (IceepeeceeException e) {
            fail("Threw exception");
        }
    }

    @Test
    void shouldGetFlightLocation() {
        try {
            simulator.addFlight("yellow", testFlights[0][0], testFlights[0][1]);
            assertArrayEquals(simulator.flightLocation("yellow"), new int[][]{testFlights[0][0], testFlights[0][1]});
        } catch (IceepeeceeException e) {
            fail("Threw exception");
        }
    }

    @Test
    void shouldNotGetLocationOfNonExistentIsland() {
        try {
            simulator.islandLocation("red");
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.ISLAND_NOT_FOUND);
        }
    }

    @Test
    void shouldNotGetLocationOfNonExistentFlight() {
        try {
            simulator.flightLocation("red");
            fail("Did not threw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.FLIGHT_NOT_FOUND);
        }
    }

//    ANGLE
    @Test
    void shouldGetAngleOfPreviouslyMadePhotograph() {
        try {
            simulator.addFlight("yellow", testFlights[0][0], testFlights[0][1]);
            simulator.photograph("yellow", 50);
            assertEquals(simulator.flightCamera("yellow"), 50);
        } catch (IceepeeceeException e) {
            fail("Threw exception");
        }
    }

    @Test
    void shouldNotGetAngleOfNonExistentFlight() {
        try {
            assertEquals(simulator.flightCamera("yellow"), 50);
            fail("Did not throw exception");
        } catch (IceepeeceeException e) {
            assertEquals(e.getMessage(), IceepeeceeException.FLIGHT_NOT_FOUND);
        }
    }
}