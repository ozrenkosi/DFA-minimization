//
// Created by ozrenkosi on 11.04.2017..
//

import java.util.*;

public class MinDka {

    private static String initialState;
    private static List<String> states = new ArrayList<>();
    private static List<String> inputStrings = new ArrayList<>();
    private static List<String> acceptedStates = new ArrayList<>();
    private static List<String[]> transitionFunction = new ArrayList<>();

    public static void main(String[] args) {

        dataInput();

        removeUnreachableStates();

        removeNondistinguishableStates();

        printOutput();

    }

    private static void dataInput() {
        String[] temporaryInput;

        Scanner reader = new Scanner(System.in);

        temporaryInput = reader.nextLine().split(",");
        states.addAll(Arrays.asList(temporaryInput));

        temporaryInput = reader.nextLine().split(",");
        inputStrings.addAll(Arrays.asList(temporaryInput));

        temporaryInput = reader.nextLine().split(",");
        acceptedStates.addAll(Arrays.asList(temporaryInput));

        initialState = reader.nextLine();

        while (reader.hasNextLine()) {
            temporaryInput = reader.nextLine().split(",|->");

            if (temporaryInput[0].equals("")) {
                break;
            }

            transitionFunction.add(temporaryInput);
        }
        reader.close();
    }

    private static void removeUnreachableStates() {
        List<String> reachableStates = new ArrayList<>();
        List<String> unreachableStates = new ArrayList<>();

        // FINDING REACHABLE states
        reachableStates.add(initialState);
        for (int i = 0; i < reachableStates.size(); i++) {
            for (int j = 0; j < transitionFunction.size(); j++) {
                for (int k = 0; k < inputStrings.size(); k++) {
                    if (transitionFunction.get(j)[0].equals(reachableStates.get(i)) && transitionFunction.get(j)[1].equals(inputStrings.get(k))) {
                        if (!reachableStates.contains(transitionFunction.get(j)[2])) {
                            reachableStates.add(transitionFunction.get(j)[2]);
                        }
                    }
                }
            }
        }

        // FINDING UNREACHABLE states
        for (int i = 0; i < states.size(); i++) {
            if (!reachableStates.contains(states.get(i)) && !unreachableStates.contains(states.get(i))) {
                unreachableStates.add(states.get(i));
            }
        }

        // REMOVING UNREACHABLE STATES
        for (int j = 0; j < unreachableStates.size(); j++) {
            int index = 0;
            for (int i = 0; i < transitionFunction.size(); i++) {
                if (transitionFunction.get(i)[0].equals(unreachableStates.get(j))) {
                    index = i;
                    break;
                }
            }
            for (int i = 0; i < inputStrings.size(); i++) {
                transitionFunction.remove(index);
            }

            states.remove(unreachableStates.get(j));
            acceptedStates.remove(unreachableStates.get(j));
        }

    }

    private static void removeNondistinguishableStates() {
        int[][] table = new int[states.size() - 1][states.size() - 1];
        List<String> statesForDeletion = new ArrayList<>();

        // MARKING PAIRS (ACCEPTED STATE, NOT ACCEPTED STATE)
        for (int i = 0; i < states.size() - 1; i++) {
            for (int j = 0; j < states.size() - 1; j++) {
                if (i < j) {
                    break;
                }
                if (acceptedStates.contains(states.get(i+1)) && !acceptedStates.contains(states.get(j))) {
                    table[i][j] = 1;
                }
                if (!acceptedStates.contains(states.get(i+1)) && acceptedStates.contains(states.get(j))) {
                    table[i][j] = 1;
                }
            }
        }

        // MINIMIZING DKA
        for (int repeat = 0; repeat < states.size()*states.size(); repeat++) {
            for (int i = 0; i < states.size() - 1; i++) {
                for (int j = 0; j < states.size() - 1; j++) {
                    if (i < j) {
                        break;
                    }
                    if (table[i][j] == 0) {
                        for (int k = 0; k < inputStrings.size(); k++) {
                            int indexI = states.indexOf(transitionFunction.get((i + 1) * inputStrings.size() + k)[2]);
                            int indexJ = states.indexOf(transitionFunction.get(j * inputStrings.size() + k)[2]);

                            if (indexI == indexJ) {
                                continue;
                            }

                            if (indexJ > indexI) {
                                int temp = indexJ;
                                indexJ = indexI;
                                indexI = temp;
                            }

                            if (table[indexI - 1][indexJ] == 1) {
                                table[i][j] = 1;
                            }
                        }
                    }
                }
            }
        }

        // TABLE DECODING
        for (int i = 0; i < states.size() - 1; i++) {
            for (int j = 0; j < states.size() - 1; j++) {
                if (i < j) {
                    break;
                }
                if (table[i][j] == 0) {
                    if (states.get(i+1).equals(initialState)) {
                        initialState = states.get(j);
                    }
                    if (states.indexOf(states.get(i+1)) > states.indexOf(states.get(j)) && !statesForDeletion.contains(states.get(i+1))) {
                        statesForDeletion.add(states.get(i+1));
                        statesForDeletion.add(states.get(j));
                    }
                    else if (!statesForDeletion.contains(states.get(j))){
                        statesForDeletion.add(states.get(j));
                        statesForDeletion.add(states.get(i+1));
                    }
                }
            }
        }

        // REMOVE STATES
        for (int j = 0; j < statesForDeletion.size(); j = j + 2) {
            int index = 0;
            for (int i = 0; i < transitionFunction.size(); i++) {
                if (transitionFunction.get(i)[0].equals(statesForDeletion.get(j))) {
                    index = i;
                    break;
                }
            }
            for (int i = 0; i < inputStrings.size(); i++) {
                transitionFunction.remove(index);
            }

            states.remove(statesForDeletion.get(j));
            acceptedStates.remove(statesForDeletion.get(j));
        }

        // CHANGING STATES FROM TRANSITION FUNCTION
        for (int i = 0; i < transitionFunction.size(); i++) {
            for (int j = 0; j < statesForDeletion.size(); j = j + 2) {
                if (transitionFunction.get(i)[2].equals(statesForDeletion.get(j))) {
                    transitionFunction.get(i)[2] = statesForDeletion.get(j+1);
                }
            }
        }

    }

    private static void printOutput() {
        String statesFormatted = states.toString()
                .replace(" ", "")
                .replace("[", "")
                .replace("]", "")
                .trim();
        System.out.println(statesFormatted);

        String inputStringsFormatted = inputStrings.toString()
                .replace(" ", "")
                .replace("[", "")
                .replace("]", "")
                .trim();
        System.out.println(inputStringsFormatted);

        String acceptedStatesFormatted = acceptedStates.toString()
                .replace(" ", "")
                .replace("[", "")
                .replace("]", "")
                .trim();
        System.out.println(acceptedStatesFormatted);

        System.out.println(initialState);

        for (int i = 0; i < transitionFunction.size(); i++) {
            System.out.print(transitionFunction.get(i)[0] + "," + transitionFunction.get(i)[1] + "->" + transitionFunction.get(i)[2] + "\n");
        }
    }


}