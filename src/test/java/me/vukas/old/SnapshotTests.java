package me.vukas.old;

import io.protostuff.GraphIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import me.vukas.graphdiff.diff.DataDiff;
import me.vukas.graphdiff.diff.Diff;
import me.vukas.graphdiff.diff.SchemaDiff;
import me.vukas.graphdiff.snapshot.Snapshot;
import me.vukas.old.model.GrandChildEntity;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SnapshotTests {

    class A{
//        int x;
//        A rec1;
//        A rec2;
//        A recis1;
//        A recis2;
//        A[] recA;
//        A[] recAR;
        A[] xA;
    }

    @Test
    public void primitEq(){
        A a = new A();
        a.xA = new A[]{a};
        A b = new A();
        b.xA = new A[]{b};

        Snapshot s1 = Snapshot.of(a);
        Snapshot s2 = Snapshot.of(b);

        Diff d = s1.compareTo(s2);
        Snapshot s3 = s1.patchWith(d);

        A c = (A) s3.materialize();
    }

    @Test
    public void recurtest(){
        A a1 = new A();
        A a2 = new A();
//        a1.x = 1;
//        a1.rec1=a1;
//        a1.rec2=a2;
//        a1.recis1=a1;
//        a1.recis2=a2;
//        a1.recA=new A[]{a1,a2};
//        a1.recAR=new A[]{a2,a1};
        a1.xA=new A[]{a1};
//        a2.x = 2;
//        a2.rec1=a2;
//        a2.rec2=a1;
//        a2.recis1=a1;
//        a2.recis2=a2;
//        a2.recA=new A[]{a1,a2};
//        a2.recAR=new A[]{a1,a2};
        a2.xA=new A[]{a1};

        Snapshot sn1 = Snapshot.of(a1);
        Snapshot sn2 = Snapshot.of(a2);

        Diff d = sn1.compareTo(sn2);

        Snapshot sn3 = sn1.patchWith(d);

        //Snapshot sn32 = sn1.patch(diffProba);

        A am = (A) sn3.materialize();

        LinkedBuffer buffer = LinkedBuffer.allocate(512);
        Schema<A> schema = RuntimeSchema.getSchema(A.class/*, STRATEGY*/);

        // ser
        final byte[] protostuffzz;
        try
        {
            protostuffzz = GraphIOUtil.toByteArray(a2, schema, buffer);
        }
        finally
        {
            buffer.clear();
        }

        final byte[] protostuff2;
        try
        {
            protostuff2 = GraphIOUtil.toByteArray(am, schema, buffer);
        }
        finally
        {
            buffer.clear();
        }
    }

    @Test
    public void patchingObjectGraphWithObjectGraphWitchCircularReferencesUsingPartialKeyShouldProduceObjectGraph2(){
        GrandChildEntity gce1 = new GrandChildEntity(1);
        GrandChildEntity gce2 = new GrandChildEntity(2);
        gce1.setParent1(gce2);
        gce1.setParent2(gce1);
        gce1.addParentInList(gce1);
        gce1.addParentInList(gce2);
        gce1.addParentInList(gce1);
        gce1.addParentInList(gce2);
        gce1.addParentInSet(gce1);
        gce1.addParentInSet(gce2);
        gce1.addParentInSet(gce1);
        gce1.addParentInSet(gce2);
        gce1.addParentInArray(0, gce1);
        gce1.addParentsInMap(gce1, gce1);
        gce1.addParentsInMap(gce2, gce1);
        gce1.setParentInterface(gce2);
        gce1.setParentAbstract(gce1);
        gce1.addParentInInterfaceArray(0, gce1);
        gce1.addParentInAbstractArray(0, gce2);
        gce1.setParentRecursiveInterfaceArray(gce1.getParentsArray());
        gce2.setParent1(gce1);
        gce2.setParent2(gce2);
        gce2.addParentInList(gce2);
        gce2.addParentInList(gce1);
        gce2.addParentInSet(gce2);
        gce2.addParentInSet(gce1);
        gce2.addParentInSet(null);
        gce2.addParentInSet(null);
        gce2.addParentInArray(0, gce2);
        gce2.addParentInArray(1, gce1);
        gce2.addParentsInMap(gce2, gce2);
        gce2.addParentsInMap(null, gce1);
        gce2.addParentsInMap(gce1, null);
        gce2.addParentsInMap(gce2, gce1);
        gce2.setParentRecursiveInterfaceArray(gce1.getParentsArray());

        GrandChildEntity gce3 = new GrandChildEntity(1);
        GrandChildEntity gce4 = new GrandChildEntity(2);
        gce3.setParent1(gce4);
        gce3.setParent2(gce3);
        gce3.addParentInList(gce3);
        gce3.addParentInList(gce4);
        gce3.addParentInList(gce3);
        gce3.addParentInList(gce4);
        gce3.addParentInSet(gce3);
        gce3.addParentInSet(gce4);
        gce3.addParentInSet(gce3);
        gce3.addParentInSet(gce4);
        gce3.addParentInArray(0, gce3);
        gce3.addParentsInMap(gce3, gce3);
        gce3.addParentsInMap(gce4, gce3);
        gce3.setParentInterface(gce4);
        gce3.setParentAbstract(gce3);
        gce3.addParentInInterfaceArray(0, gce3);
        gce3.addParentInAbstractArray(0, gce4);
        gce3.setParentRecursiveInterfaceArray(gce3.getParentsArray());
        gce4.setParent1(gce3);
        gce4.setParent2(gce4);
        gce4.addParentInList(gce4);
        gce4.addParentInList(gce3);
        gce4.addParentInSet(gce4);
        gce4.addParentInSet(gce3);
        gce4.addParentInSet(null);
        gce4.addParentInSet(null);
        gce4.addParentInArray(0, gce4);
        gce4.addParentInArray(1, gce3);
        gce4.addParentsInMap(gce4, gce4);
        gce4.addParentsInMap(null, gce3);
        gce4.addParentsInMap(gce3, null);
        gce4.addParentsInMap(gce4, gce3);
        gce4.setParentRecursiveInterfaceArray(gce3.getParentsArray());

        Snapshot snapshot1 = Snapshot.of(gce1);
        Snapshot snapshot2 = Snapshot.of(gce2);

        Diff diff = snapshot1.compareTo(snapshot2);

        Snapshot snapshot3 = snapshot1.patchWith(diff);

        GrandChildEntity gceDiff = (GrandChildEntity) snapshot3.materialize();
        //GrandChildEntity gceDiff = snapshot2.materialize(GrandChildEntity.class);

//        Schema diffNodeSchema = RuntimeSchema.getSchema(d.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(512);
//        final byte[] protostuff;
//        try
//        {
//            protostuff = GraphIOUtil.toByteArray(d, diffNodeSchema, buffer);
//        }
//        finally
//        {
//            buffer.clear();
//        }
//
//        Objenesis objenesis = new ObjenesisStd();
//        ObjectInstantiator<?> instantiator = objenesis.getInstantiatorOf(d.getClass());
//        Object fin = instantiator.newInstance();
//        GraphIOUtil.mergeFrom(protostuff, fin, diffNodeSchema);
//
//        Object p = d.patch(gce3);
//
        Schema<GrandChildEntity> schema = RuntimeSchema.getSchema(GrandChildEntity.class/*, STRATEGY*/);

        // ser
        final byte[] protostuffzz;
        try
        {
            protostuffzz = GraphIOUtil.toByteArray(gce2, schema, buffer);
        }
        finally
        {
            buffer.clear();
        }

        final byte[] protostuff2;
        try
        {
            protostuff2 = GraphIOUtil.toByteArray(gceDiff, schema, buffer);
        }
        finally
        {
            buffer.clear();
        }

        assertThat(protostuffzz).isEqualTo(protostuff2);


    }
}
