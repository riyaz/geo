package com.github.davidmoten.geo;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class GeoHashTest {

	private static final double PRECISION = 0.000000001;

	@Test
	public void testWhiteHouseHashEncode() {
		assertEquals("dqcjqcp84c6e",
				GeoHash.encodeHash(38.89710201881826, -77.03669792041183));
	}

	@Test
	public void testWhiteHouseHashDecode() {
		LatLong point = GeoHash.decodeHash("dqcjqcp84c6e");
		assertEquals(point.getLat(), 38.89710201881826, PRECISION);
		assertEquals(point.getLon(), -77.03669792041183, PRECISION);
	}

	@Test
	public void testFromGeoHashDotOrg() {
		assertEquals("6gkzwgjzn820", GeoHash.encodeHash(-25.382708, -49.265506));
	}

	@Test
	public void testHashOfNonDefaultLength() {
		assertEquals("6gkzwg", GeoHash.encodeHash(-25.382708, -49.265506, 6));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashEncodeGivenNonPositiveLength() {
		GeoHash.encodeHash(-25.382708, -49.265506, 0);
	}

	@Test
	public void testAnother() {
		assertEquals("sew1c2vs2q5r", GeoHash.encodeHash(20, 31));
	}

	@Test
	public void testAdjacentBottom() {
		assertEquals("u0zz", GeoHash.adjacentHash("u1pb", Direction.BOTTOM));
	}

	@Test
	public void testAdjacentTop() {
		assertEquals("u1pc", GeoHash.adjacentHash("u1pb", Direction.TOP));
	}

	@Test
	public void testAdjacentLeft() {
		assertEquals("u1p8", GeoHash.adjacentHash("u1pb", Direction.LEFT));
	}

	@Test
	public void testAdjacentRight() {
		assertEquals("u300", GeoHash.adjacentHash("u1pb", Direction.RIGHT));
	}

	@Test
	public void testNeighbouringHashes() {
		String center = "dqcjqc";
		Set<String> neighbours = Sets.newHashSet("dqcjqf", "dqcjqb", "dqcjr1",
				"dqcjq9", "dqcjqd", "dqcjr4", "dqcjr0", "dqcjq8");
		assertEquals(neighbours, Sets.newHashSet(GeoHash.neighbours(center)));
	}

	@Test
	public void testHashDecodeOnBlankString() {
		LatLong point = GeoHash.decodeHash("");
		assertEquals(0, point.getLat(), PRECISION);
		assertEquals(0, point.getLon(), PRECISION);
	}

	@Test
	public void testInstantiation() {
		GeoHash.instantiate();
	}

	@Test
	public void testSpeed() {
		long t = System.currentTimeMillis();
		int numIterations = 100000;
		for (int i = 0; i < numIterations; i++)
			GeoHash.encodeHash(38.89710201881826, -77.03669792041183);
		double numPerSecond = numIterations / (System.currentTimeMillis() - t)
				* 1000;
		System.out.println("num encodeHash per second=" + numPerSecond);

	}

	@Test
	public void testHashLengthCalculationForZeroSeparationDistance() {
		assertEquals(
				11,
				GeoHash.minHashLengthToEnsureCellCentreSeparationDistanceIsLessThanMetres(0));
	}

	@Test
	public void testHashLengthCalculationWhenVeryLargeSeparationDistance() {
		assertEquals(
				1,
				GeoHash.minHashLengthToEnsureCellCentreSeparationDistanceIsLessThanMetres(5003530 * 2));
	}

	@Test
	public void testHashLengthCalculationWhenMediumDistance() {
		assertEquals(
				5,
				GeoHash.minHashLengthToEnsureCellCentreSeparationDistanceIsLessThanMetres(3900));
	}

	@Test
	public void testCoverBoundingBox() {
		for (String hash : GeoHash.hashesToCoverBoundingBox(0, 135, 10, 145, 1)) {
			System.out.println(GeoHash.decodeHash(hash));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCoverBoundingBoxMustBePassedMinHashesGreaterThanZero() {
		GeoHash.hashesToCoverBoundingBox(0, 135, 10, 145, 0);
	}
}
