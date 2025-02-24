/**
 * 
 */
package it.unicam.cs.asdl2425.pt1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Test per la classe AVLTreeSort.
 * 
 * @author Luca Tesei
 *         (template) **INSERIRE NOME, COGNOME ED EMAIL xxxx@studenti.unicam.it DELLO STUDENTE**
 */
class AVLTreeSortTest {

    @Test
    final void testSortAlreadySorted() {
        AVLTreeSort<Integer> sorter = new AVLTreeSort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        SortingAlgorithmResult<Integer> result = sorter.sort(inputList);
        
        // Controlla che la lista risultante sia ordinata
        assertTrue(result.checkOrder(), "La lista dovrebbe essere ordinata in modo crescente");
        // Controlla che il risultato corrisponda a quello atteso
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(expected, result.getL(), "Lista già ordinata non corrisponde all'attesa");
    }
    
    @Test
    final void testSortReverseSorted() {
        AVLTreeSort<Integer> sorter = new AVLTreeSort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
        SortingAlgorithmResult<Integer> result = sorter.sort(inputList);
        
        // La lista ordinata in ordine crescente
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(expected, result.getL(), "La lista decrescente non è stata ordinata correttamente");
        assertTrue(result.checkOrder(), "La lista non è ordinata in ordine crescente");
    }
    
    @Test
    final void testSortWithDuplicates() {
        AVLTreeSort<Integer> sorter = new AVLTreeSort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(5, 3, 7, 3, 8, 5, 3));
        SortingAlgorithmResult<Integer> result = sorter.sort(inputList);
        
        // L'ordine atteso è 3,3,3,5,5,7,8
        List<Integer> expected = Arrays.asList(3, 3, 3, 5, 5, 7, 8);
        assertEquals(expected, result.getL(), "La lista ordinata con duplicati non corrisponde all'attesa");
        assertTrue(result.checkOrder(), "La lista con duplicati non è ordinata in modo crescente");
    }
    
    @Test
    final void testSortSingleElement() {
        AVLTreeSort<Integer> sorter = new AVLTreeSort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(42));
        SortingAlgorithmResult<Integer> result = sorter.sort(inputList);
        
        List<Integer> expected = Arrays.asList(42);
        assertEquals(expected, result.getL(), "Lista con un solo elemento non ordinata correttamente");
        assertTrue(result.checkOrder(), "Lista con un solo elemento non è stata riconosciuta come ordinata");
    }
    
    @Test
    final void testSortEmptyList() {
        AVLTreeSort<Integer> sorter = new AVLTreeSort<>();
        List<Integer> inputList = new ArrayList<>();
        SortingAlgorithmResult<Integer> result = sorter.sort(inputList);
        
        assertTrue(result.getL().isEmpty(), "La lista risultante dovrebbe essere vuota");
        // In una lista vuota non vengono effettuati confronti
        assertEquals(0, result.getCountCompare(), "Il numero di confronti per una lista vuota dovrebbe essere 0");
    }
    
    @Test
    final void testSortNullInput() {
        AVLTreeSort<Integer> sorter = new AVLTreeSort<>();
        // Se la lista è null, deve essere lanciata una NullPointerException
        assertThrows(NullPointerException.class, () -> sorter.sort(null), 
                "Dovrebbe lanciare NullPointerException per input null");
    }
    
    @Test
    final void testSortWithNullElements() {
        AVLTreeSort<Integer> sorter = new AVLTreeSort<>();
        List<Integer> inputList = new ArrayList<>(Arrays.asList(1, null, 3, 2));
        // Se la lista contiene elementi null, deve essere lanciata una NullPointerException
        assertThrows(NullPointerException.class, () -> sorter.sort(inputList), 
                "L'algoritmo non deve accettare liste con elementi null");
    }
}
