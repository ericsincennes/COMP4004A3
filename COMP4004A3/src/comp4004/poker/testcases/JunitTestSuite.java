package comp4004.poker.testcases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
@RunWith(Suite.class)
@Suite.SuiteClasses({
   BoardStateTest.class,
   CardTest.class,
   DeckTest.class,
   HandTest.class,
   PlayerTests.class,
   PointsBoardTest.class,
})
public class JunitTestSuite {   
} 
