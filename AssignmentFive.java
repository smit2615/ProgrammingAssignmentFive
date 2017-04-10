import java.util.Scanner;
import java.io.*;

public class AssignmentFive {

    private BinarySearchTree<String>[] dictionary = new BinarySearchTree[26]; //array of BST's'
    private long found = 0; //number of words found
    private long foundComps = 0; //number of comparisons of words found
    private long notFound = 0; //number of words not found 
    private long notFoundComps = 0; //number of comparisons of words not found

    public AssignmentFive() {
        for(int i = 0; i < dictionary.length; i++)
            dictionary[i] = new BinarySearchTree<String>();
    }

    /**
     * Precondition: a dictionary File and an array of length 26
     * Postcondition: the BST's' are loaded
     * with every word of the appropriate letter
     * in the File
     */
    public void loadDictionary() {
        File dict = new File("random_dictionary.txt");
        try {
            Scanner in = new Scanner(dict);
            while(in.hasNext()) {
                String word = in.next().toLowerCase();
                dictionary[word.charAt(0) - 97].insert(word);
            }
            in.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Precondition: a book file and a loaded dictionary
     * Postcondition: the number of words found and not found,
     * the number of comparisons of words found and not found,
     * and the average number of comparisons of words found 
     * and not found
     *
     * Uses overloaded search method and checks two special
     * cases concerning hyphens and apostrophes
     */
    public void spellCheck() {
        File book = new File("oliver.txt");
        int[] i = {0}; //passed into search method to hold number of comparisons
        
        try {
            Scanner in = new Scanner(book);
            while(in.hasNext()) {
                String word = in.next().toLowerCase().replaceAll("[^a-z\\-']", ""); //eliminate all non a-z, hyphens, or apostrophes
                
                if(word.isEmpty()) //was not a valid word to check 
                    continue; //this is done to keep the notFound from adding tons of unnecessary words
                
                if(word.contains("-")) {
                    for(String words : word.split("-")) { //get everything on either side of the hyphens
                        String test = words.replaceAll("('[a-z]+)|[^a-z]", ""); //replace all non a-z to the right of the apostrophe,   
                        if(test.isEmpty())                                      //further replace any non a-z after that
                            continue;   
                        if(dictionary[test.charAt(0) - 97].search(test, i)) {
                            found++;
                            foundComps += i[0];
                            i[0] = 0; //resets count array beacause overloaded search does not 
                        }             // reset it like overloaded conains did in AssignmentFour  
                        else {
                            notFound++;
                            notFoundComps += i[0];
                            i[0] = 0;
                        }
                    }
                }

                else if(word.contains("'")) {
                    String front = word.replaceAll("('[a-z]+)|[^a-z]", ""); //see above
                    if(front.isEmpty())
                        continue;
                    if(dictionary[front.charAt(0) - 97].search(front, i)) {
                        found++;
                        foundComps += i[0];
                        i[0] = 0;
                    }
                    else {
                        notFound++;
                        notFoundComps += i[0];
                        i[0] = 0;
                    }
                }
                
                else if(dictionary[word.charAt(0) - 97].search(word, i)) {
                    found++;
                    foundComps += i[0];
                    i[0] = 0;
                }
                
                else {
                    notFound++;
                    notFoundComps += i[0];
                    i[0] = 0;
                }
            }
            in.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Purpose: tests the methods implemented
     * in AssignmentFive.java
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        long start = System.nanoTime();
        AssignmentFive a = new AssignmentFive();
        a.loadDictionary();
        a.spellCheck();
        
        System.out.println("Words found is " + a.found);
        System.out.println("Words not found is " + a.notFound);
        System.out.println("Number of comparisons of words found is " + a.foundComps);
        System.out.println("Number of comparisons of words not found is " + a.notFoundComps);
        System.out.printf("The average number of comparisons of words found is %.2f", (double) a.foundComps / a.found);
        System.out.printf("\nThe average number of comparisons of words not found is %.2f", (double) a.notFoundComps / a.notFound);
        System.out.printf("\nRuntime: %.4f seconds\n", (System.nanoTime() - start) / Math.pow(10, 9)); 
    }
}

/**
 * overloaded search method
 * Precondition: an element and a single element int array
 * Postcondition: true or false depending on the element being found
 *
 * @param e the element to be searched
 * @param count holds the number of comparisons
 * @return boolean true or false if the element is found or not
 *
 * public boolean search(E e, int[] count) {
 *   TreeNode<E> current = root; // Start from the root
 *   while (current != null) {
 *     if (e.compareTo(current.element) < 0) {
 *       current = current.left;
 *     }
 *     else if (e.compareTo(current.element) > 0) {
 *       current = current.right;
 *     }
 *     else { // element matches current.element
 *      count[0]++;
 *       return true; // Element is found
 *     }
 *     count[0]++;
 *   }
 *   return false;
 * }
 */