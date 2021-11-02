package models;

public class DisjunctionPropositionTest {
    public static void main(String[] args) {
        var prop1 = new models.DisjunctionProposition("AvB");
        var prop2 = new models.DisjunctionProposition("Cv!B");
        assert prop1.resolve(prop2).toString().equals("AvC");

        prop1 = new models.DisjunctionProposition("AvBvC");
        prop2 = new models.DisjunctionProposition("!CvDvE");
        assert prop1.resolve(prop2).toString().equals("AvBvDvE");

        prop1 = new models.DisjunctionProposition("A");
        prop2 = new models.DisjunctionProposition("!AvB");
        assert prop1.resolve(prop2).toString().equals("B");

        prop1 = new models.DisjunctionProposition("A");
        prop2 = new models.DisjunctionProposition("!AvB");
        assert prop1.resolve(prop2).toString().equals("B");

        prop1 = new models.DisjunctionProposition("!E");
        prop2 = new models.DisjunctionProposition("!Cv!DvE");
        assert prop1.resolve(prop2).toString().equals("!Cv!D");

        prop1 = new models.DisjunctionProposition("!E");
        prop2 = new models.DisjunctionProposition("E");
        try {
            prop1.resolve(prop2);
            assert false;
        } catch (Exception e) {
            assert true;
        }

        prop1 = new models.DisjunctionProposition("!E");
        prop2 = new models.DisjunctionProposition("!Cv!DvA");
        assert prop1.resolve(prop2) == null;
    }
}
