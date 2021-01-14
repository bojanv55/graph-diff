package me.vukas.old;

import me.vukas.graphdiff.diff.Diff;
import me.vukas.graphdiff.snapshot.Snapshot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DefaultTests {
    //primitives
    private byte primitiveByte;
    private short primitiveShort;
    private int primitiveInt;
    private long primitiveLong;
    private float primitiveFloat;
    private double primitiveDouble;
    private boolean primitiveBoolean;
    private char primitiveChar;

    //null reference
    private Object nullReference;

    private final EmptyInterface secondLevelCycle;
    {
        CyclicObject1 cyclicObject1 = new CyclicObject1();
        secondLevelCycle = new CyclicObject2(cyclicObject1);
        cyclicObject1.setCycle(secondLevelCycle);
    }

    private final List<Object> all = List.of(
            //empty primitive arrays
            new byte[0],
            new short[0],
            new int[0],
            new long[0],
            new float[0],
            new double[0],
            new boolean[0],
            new char[0],

            //empty boxed arrays
            new Byte[0],
            new Short[0],
            new Integer[0],
            new Long[0],
            new Float[0],
            new Double[0],
            new Boolean[0],
            new Character[0],
            new Object[0],
            new String[0],
            new EmptyInterface[0],
            new EmptyObject1[0],

            //primitive arrays with few elements
            new byte[]{1, 2, 3},
            new short[]{1, 2, 3},
            new int[]{1, 2, 3},
            new long[]{1, 2, 3},
            new float[]{1, 2, 3},
            new double[]{1, 2, 3},
            new boolean[]{false, true, false},
            new char[]{1, 2, 3},

            //boxed arrays with few elements
            new Byte[]{1, 2, 3},
            new Short[]{1, 2, 3},
            new Integer[]{1, 2, 3},
            new Long[]{1L, 2L, 3L},
            new Float[]{1F, 2F, 3F},
            new Double[]{1D, 2D, 3D},
            new Boolean[]{true, false, true},
            new Character[]{1, 2, 3},
            new Object[]{1, 2, 3},
            new String[]{"1", "2", "3"},
            new EmptyInterface[]{new EmptyObject1(), new EmptyObject2(), new EmptyObject1()},
            new EmptyObject1[]{new EmptyObject1(), new EmptyObject1(), new EmptyObject1()},

            //empty primitive 2D arrays
            new byte[0][],
            new short[0][],
            new int[0][],
            new long[0][],
            new float[0][],
            new double[0][],
            new boolean[0][],
            new char[0][],

            //empty boxed 2D arrays
            new Byte[0][],
            new Short[0][],
            new Integer[0][],
            new Long[0][],
            new Float[0][],
            new Double[0][],
            new Boolean[0][],
            new Character[0][],
            new Object[0][],
            new String[0][],
            new EmptyInterface[0][],
            new EmptyObject1[0][],

            //primitive 2D arrays with few elements
            new byte[][]{new byte[]{1, 2, 3}, new byte[]{4, 5, 6}, new byte[]{7, 8, 9}},
            new short[][]{new short[]{1, 2, 3}, new short[]{4, 5, 6}, new short[]{7, 8, 9}},
            new int[][]{new int[]{1, 2, 3}, new int[]{4, 5, 6}, new int[]{7, 8, 9}},
            new long[][]{new long[]{1, 2, 3}, new long[]{4, 5, 6}, new long[]{7, 8, 9}},
            new float[][]{new float[]{1, 2, 3}, new float[]{4, 5, 6}, new float[]{7, 8, 9}},
            new double[][]{new double[]{1, 2, 3}, new double[]{4, 5, 6}, new double[]{7, 8, 9}},
            new boolean[][]{new boolean[]{false, true, false}, new boolean[]{false, true, false}, new boolean[]{false, true, false}},
            new char[][]{new char[]{1, 2, 3}, new char[]{4, 5, 6}, new char[]{7, 8, 9}},

            //boxed 2D arrays with few elements
            new Byte[][]{new Byte[]{1, 2, 3}, new Byte[]{4, 5, 6}, new Byte[]{7, 8, 9}},
            new Short[][]{new Short[]{1, 2, 3}, new Short[]{4, 5, 6}, new Short[]{7, 8, 9}},
            new Integer[][]{new Integer[]{1, 2, 3}, new Integer[]{4, 5, 6}, new Integer[]{7, 8, 9}},
            new Long[][]{new Long[]{1L, 2L, 3L}, new Long[]{4L, 5L, 6L}, new Long[]{7L, 8L, 9L}},
            new Float[][]{new Float[]{1F, 2F, 3F}, new Float[]{4F, 5F, 6F}, new Float[]{7F, 8F, 9F}},
            new Double[][]{new Double[]{1D, 2D, 3D}, new Double[]{4D, 5D, 6D}, new Double[]{7D, 8D, 9D}},
            new Boolean[][]{new Boolean[]{false, true, false}, new Boolean[]{false, true, false}, new Boolean[]{false, true, false}},
            new Character[][]{new Character[]{1, 2, 3}, new Character[]{4, 5, 6}, new Character[]{7, 8, 9}},
            new Object[][]{new Object[]{1, 2, 3}, new Object[]{4, 5, 6}, new Object[]{7, 8, 9}},
            new String[][]{new String[]{"1", "2", "3"}, new String[]{"4", "5", "6"}, new String[]{"7", "8", "9"}},
            new EmptyInterface[][]{
                    new EmptyInterface[]{new EmptyObject1(), new EmptyObject2(), new EmptyObject1()},
                    new EmptyObject1[]{new EmptyObject1(), new EmptyObject1(), new EmptyObject1()},
                    new EmptyObject2[]{new EmptyObject2(), new EmptyObject2(), new EmptyObject2()}
            },
            new EmptyObject1[][]{
                    new EmptyObject1[]{new EmptyObject1(), new EmptyObject1(), new EmptyObject1()},
                    new EmptyObject1[]{new EmptyObject1(), new EmptyObject1(), new EmptyObject1()},
                    new EmptyObject1[]{new EmptyObject1(), new EmptyObject1(), new EmptyObject1()}
            },

            //objects
            new EmptyObject1(),
            new CyclicObject1(),
            secondLevelCycle
    );

    @Test
    public void diffingAllWithAll() {
        all.forEach(f1 -> all.forEach(f2 -> {
            Snapshot s1 = Snapshot.of(f1);
            Snapshot s2 = Snapshot.of(f2);
            Diff d = s1.compareTo(s2);
            Snapshot s3 = s1.patchWith(d);
            Object f3 = s3.materialize();
            assertThat(f2).usingRecursiveComparison().isEqualTo(f3);
        }));
    }
}
