package com.vityarthi.slawme;

/**
 * Main Standalone Unit Test Runner.
 */
public class TestRunner {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  SLAWME AUTOMATED UNIT TEST SUITE RUNNER         ");
        System.out.println("==================================================");
        try {
            AuthServiceTest.runAllTests();
            InventoryServiceTest.runAllTests();
            System.out.println("\n==================================================");
            System.out.println("  SUMMARY: ALL UNIT TESTS COMPLETED SUCCESSFULLY!  ");
            System.out.println("==================================================");
        } catch (Throwable t) {
            System.err.println("\n❌ TEST SUITE FAILURE: " + t.getMessage());
            t.printStackTrace();
            System.exit(1);
        }
    }
}
