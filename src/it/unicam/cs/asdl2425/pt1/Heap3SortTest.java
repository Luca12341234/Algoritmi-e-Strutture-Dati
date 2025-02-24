/**
 *
 */
package it.unicam.cs.asdl2425.pt1;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di Test per Heap3Sort.
 * 
 * @author Luca Tesei
 */
class Heap3SortTest {


    @Test
    final void testName() {
        Heap3Sort<Integer> sort_test = new Heap3Sort<Integer>();
        assertEquals(sort_test.getName(), "Heap3Sort");
    }

    @Test
    final void testInteger() {
        Heap3Sort<Integer> sort_test = new Heap3Sort<Integer>();
        List<Integer> test_list = new ArrayList<Integer>();
        test_list.add(6);
        test_list.add(7);
        test_list.add(2);
        test_list.add(11);
        test_list.add(4);
        test_list.add(13);
        test_list.add(1);
        List<Integer> result = sort_test.sort(test_list).getL();
        assert (result.get(0) == 1);
        assert (result.get(2) == 4);
        assertTrue(sort_test.sort(test_list).checkOrder());
        for (Integer i : sort_test.sort(test_list).getL()) {
            System.out.println(i);
        }
    }

    @Test
    final void testIsEmpty() {
        Heap3Sort<Integer> sort_test = new Heap3Sort<>();
        List<Integer> test_list = new ArrayList<>();

        SortingAlgorithmResult<Integer> result = sort_test.sort(test_list);
        assertEquals(result.getL(), test_list);
        assertEquals(result.getL().size(), 0);
    }

    @Test
    void testEmptyList() {
        Heap3Sort<Integer> heapSort = new Heap3Sort<>();
        List<Integer> inputList = new ArrayList<>();

        SortingAlgorithmResult<Integer> result = heapSort.sort(inputList);

        assertTrue(result.getL().isEmpty());
        assertEquals(0, result.getCountCompare());
    }

    @Test
    void testSortedAscending() {
        Heap3Sort<Integer> heapSort = new Heap3Sort<>();
        List<Integer> inputList = new ArrayList<>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        SortingAlgorithmResult<Integer> result = heapSort.sort(inputList);

        assertTrue(result.checkOrder()); // Check if the result list is sorted
                                         // in ascending order
        assertEquals(42, result.getCountCompare()); // Update this count value
                                                    // based on your actual
                                                    // implementation

    }
    
    @Test
    void testDuplicates() {
        Heap3Sort<Integer> heapSort = new Heap3Sort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(4, 2, 7, 2, 9, 2, 4, 7));

        SortingAlgorithmResult<Integer> result = heapSort.sort(inputList);

        List<Integer> expectedList = new ArrayList<>(Arrays.asList(2, 2, 2, 4, 4, 7, 7, 9));
        assertEquals(expectedList, result.getL(), "Lista ordinata con duplicati non corretta.");
        assertTrue(result.checkOrder(), "Lista non è ordinata in ordine crescente.");
    }
    
    @Test
    void testAlreadySorted() {
        Heap3Sort<Integer> heapSort = new Heap3Sort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        SortingAlgorithmResult<Integer> result = heapSort.sort(inputList);

        List<Integer> expectedList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        assertEquals(expectedList, result.getL(), "Lista ordinata non corrisponde all'attesa.");
        assertTrue(result.checkOrder(), "Lista già ordinata non è stata riconosciuta come tale.");
    }
    
    @Test
    void testReverseSorted() {
        Heap3Sort<Integer> heapSort = new Heap3Sort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1));

        SortingAlgorithmResult<Integer> result = heapSort.sort(inputList);

        List<Integer> expectedList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        assertEquals(expectedList, result.getL(), "Lista decrescente non è stata ordinata correttamente.");
        assertTrue(result.checkOrder(), "Lista non è ordinata in ordine crescente.");
    }
    
    @Test
    void testSingleElement() {
        Heap3Sort<Integer> heapSort = new Heap3Sort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(42));

        SortingAlgorithmResult<Integer> result = heapSort.sort(inputList);

        List<Integer> expectedList = new ArrayList<>(Arrays.asList(42));
        assertEquals(expectedList, result.getL(), "Lista con un solo elemento non ordinata correttamente.");
        assertTrue(result.checkOrder(), "Lista con un solo elemento non è stata riconosciuta come ordinata.");
    }
    
    @Test
    void testStringSorting() {
        Heap3Sort<String> heapSort = new Heap3Sort<>();
        List<String> inputList = new ArrayList<>(Arrays.asList("apple", "banana", "cherry", "apple", "date", "banana"));

        SortingAlgorithmResult<String> result = heapSort.sort(inputList);

        List<String> expectedList = new ArrayList<>(Arrays.asList("apple", "apple", "banana", "banana", "cherry", "date"));
        assertEquals(expectedList, result.getL(), "Lista di stringhe non è stata ordinata correttamente.");
        assertTrue(result.checkOrder(), "Lista di stringhe non è ordinata.");
    }
    
    @Test
    void testNegativeNumbers() {
        Heap3Sort<Integer> heapSort = new Heap3Sort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(-3, -1, -7, 4, 0, 2, -5));

        SortingAlgorithmResult<Integer> result = heapSort.sort(inputList);

        List<Integer> expectedList = new ArrayList<>(Arrays.asList(-7, -5, -3, -1, 0, 2, 4));
        assertEquals(expectedList, result.getL(), "Numeri negativi non ordinati correttamente.");
        assertTrue(result.checkOrder(), "Lista con numeri negativi non è ordinata in ordine crescente.");
    }

    @Test
    void testNullElements() {
        Heap3Sort<Integer> heapSort = new Heap3Sort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(1, null, 3, 2));

        assertThrows(NullPointerException.class, () -> heapSort.sort(inputList), "L'algoritmo non ha gestito correttamente i valori null.");
    }
}
