package com.imsweb.x12;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LoopTest {

    @Test
    public void testLoop() {
        Loop loop = new Loop(new Separators('~', '*', ':'), "ISA");
        Assert.assertNotNull(loop);
        Assert.assertEquals(new Separators('~', '*', ':'), loop.getSeparators());
        Assert.assertEquals(new Separators(), loop.getSeparators());

        loop = new Loop("ISA");
        Assert.assertEquals(new Separators('~', '*', ':'), loop.getSeparators());
        Assert.assertEquals(new Separators(), loop.getSeparators());

        loop = new Loop(new Separators('A', 'B', 'C'), "ISA");
        Assert.assertEquals(new Separators('A', 'B', 'C'), loop.getSeparators());
    }

    @Test
    public void testAddChildString() {
        Loop loop = new Loop("ISA");
        Loop child = loop.addLoop("GS");
        Assert.assertNotNull(child);
    }

    @Test
    public void testAddChildIntLoop() {
        Loop loop = new Loop("ISA");
        Loop gs = new Loop("GS");
        Loop st = new Loop("ST");
        loop.addLoop(0, gs);
        loop.addLoop(1, st);
        Assert.assertEquals("ST", loop.getLoop(1).getId());
    }

    @Test
    public void testAddSegment() {
        Loop loop = new Loop("ST");
        Segment s = loop.addSegment();
        Assert.assertNotNull(s);
    }

    @Test
    public void testAddSegmentString() {
        Loop loop = new Loop("ST");
        loop.addSegment("ST*835*000000001");
        Assert.assertEquals("ST", loop.getSegment(0).getId());
    }

    @Test
    public void testAddSegmentSegment() {
        Loop loop = new Loop("ST");
        Segment segment = new Segment(new Separators('~', '*', ':'));
        segment.addElements("ST*835*000000001");
        loop.addSegment(segment);
        Assert.assertEquals("ST", loop.getSegment(0).getId());
    }

    @Test
    public void testAddSegmentInt() {
        Loop loop = new Loop("ST");
        loop.addSegment("BPR*DATA*NOT*VALID*RANDOM*TEXT");
        loop.addSegment("TRN*1*0000000000*1999999999");
        loop.addSegment("DTM*111*20090915");
        Segment segment = new Segment(new Separators('~', '*', ':'));
        segment.addElements("ST*835*000000001");
        loop.addSegment(0, segment);
        Assert.assertEquals("ST", loop.getSegment(0).getId());
    }

    @Test
    public void testAddSegmentIntString() {
        Loop loop = new Loop("ST");
        loop.addSegment("BPR*DATA*NOT*VALID*RANDOM*TEXT");
        loop.addSegment("TRN*1*0000000000*1999999999");
        loop.addSegment("DTM*111*20090915");
        loop.addSegment(0, "ST*835*000000001");
        Assert.assertEquals("ST", loop.getSegment(0).getId());
    }

    @Test
    public void testAddSegmentIntSegment() {
        Loop loop = new Loop("ST");
        loop.addSegment("ST*835*000000001");
        loop.addSegment("BPR*DATA*NOT*VALID*RANDOM*TEXT");
        loop.addSegment("DTM*111*20090915");
        Segment segment = new Segment(new Separators('~', '*', ':'));
        segment.addElements("ST*835*000000001");
        loop.addSegment(2, "TRN*1*0000000000*1999999999");
        Assert.assertEquals("TRN", loop.getSegment(2).getId());
    }

    @Test
    public void testAddChildIntString() {
        Loop loop = new Loop("ISA");
        loop.addLoop("GS");
        loop.addLoop(1, "ST");
        Assert.assertEquals("ST", loop.getLoop(1).getId());
    }

    @Test
    public void testHasLoop() {
        Loop loop = new Loop("ISA");
        loop.addLoop("GS");
        loop.addLoop("ST");
        Assert.assertEquals(true, loop.hasLoop("ST"));
    }

    @Test
    public void testFindLoop() {
        Loop loop = new Loop("ISA");
        loop.addLoop("GS");
        loop.addLoop("ST");
        loop.addLoop("1000A");
        loop.addLoop("1000B");
        loop.addLoop("2000");
        loop.addLoop("2100");
        loop.addLoop("2110");
        loop.addLoop("GE");
        loop.addLoop("IEA");
        List<Loop> loops = loop.findLoop("2000");
        Assert.assertEquals(1, loops.size());
    }

    @Test
    public void testFindSegment() {
        Loop loop = new Loop("ISA");
        loop.addLoop("GS");
        loop.addLoop("ST");
        loop.addLoop("1000A");
        loop.addLoop("1000B");
        Loop child1 = loop.addLoop("2000");
        child1.addSegment("LX*1");
        Loop child2 = loop.addLoop("2000");
        child2.addSegment("LX*2");
        loop.addLoop("2100");
        loop.addLoop("2110");
        loop.addLoop("GE");
        loop.addLoop("IEA");
        List<Segment> segments = loop.findSegment("LX");
        Assert.assertEquals(0, segments.size());

        loop = loop.getLoop("1000A");
        segments = loop.findSegment("LX");
        Assert.assertEquals(0, segments.size());

        loop = child1;
        segments = loop.findSegment("LX");
        Assert.assertEquals(1, segments.size());

        loop = child2;
        segments = loop.findSegment("LX");
        Assert.assertEquals(1, segments.size());
    }

    @Test
    public void testGetSeparators() {
        Loop loop = new Loop("ISA");
        Assert.assertEquals("[~,*,:]", loop.getSeparators().toString());
    }

    @Test
    public void testGetLoop() {
        Loop loop = new Loop("X12");
        loop.addLoop("ISA");
        loop.addLoop("GS");
        loop.addLoop("ST");
        loop.addLoop("1000A");
        loop.addLoop("1000B");
        loop.addLoop("2000");
        loop.addLoop("2100");
        loop.addLoop("2110");
        loop.addLoop("GE");
        loop.addLoop("IEA");
        Assert.assertEquals("1000A", loop.getLoop(3).getId());
    }

    @Test
    public void testGetSegment() {
        Loop loop = new Loop("ST");
        loop.addSegment("ST*835*000000001");
        loop.addSegment("BPR*DATA*NOT*VALID*RANDOM*TEXT");
        loop.addSegment("DTM*111*20090915");
        Assert.assertEquals("DTM", loop.getSegment(2).getId());
    }

    @Test
    public void testGetName() {
        Loop loop = new Loop("ST");
        Assert.assertEquals("ST", loop.getId());
    }

    @Test
    public void testIterator() {
        Loop loop = new Loop("ST");
        Assert.assertNotNull(loop.iterator());
    }

    @Test
    public void testRemoveLoop() {
        Loop loop = new Loop("X12");
        loop.addLoop("ISA");
        loop.addLoop("GS");
        loop.addLoop("ST");
        loop.addLoop("1000A");
        loop.addLoop("1000B");
        loop.addLoop("2000");
        loop.addLoop("2100");
        loop.addLoop("2110");
        loop.addLoop("SE");
        loop.addLoop("GE");
        loop.addLoop("IEA");

        Loop l1 = loop.removeLoop(3);
        Assert.assertEquals("1000A", l1.getId());

        Loop l2 = loop.removeLoop(0);
        Assert.assertEquals("ISA", l2.getId());
    }

    @Test
    public void testRemoveSegment() {
        Loop loop = new Loop("ST");
        loop.addSegment("BPR*DATA*NOT*VALID*RANDOM*TEXT");
        loop.addSegment("TRN*1*0000000000*1999999999");
        loop.addSegment("DTM*111*20090915");
        loop.addSegment(0, "ST*835*000000001");

        Segment s = loop.removeSegment(2);
        Assert.assertEquals("TRN*1*0000000000*1999999999", s.toString());
        Assert.assertEquals(3, loop.size());
    }

    @Test
    public void testChildList() {
        Loop loop = new Loop("X12");
        loop.addLoop("ISA");
        loop.addLoop("GS");
        loop.addLoop("ST");
        loop.addLoop("1000A");
        loop.addLoop("1000B");
        loop.addLoop("2000");
        loop.addLoop("2100");
        loop.addLoop("2110");
        loop.addLoop("SE");
        loop.addLoop("GE");
        loop.addLoop("IEA");
        List<Loop> loops = loop.getLoops();
        Assert.assertEquals(11, loops.size());
    }

    @Test
    public void testSize() {
        Loop loop = new Loop("ST");
        loop.addSegment("ST*835*000000001");
        loop.addSegment("BPR*DATA*NOT*VALID*RANDOM*TEXT");
        loop.addSegment("DTM*111*20090915");
        Assert.assertEquals(3, loop.size());
    }

    @Test
    public void testSetSeparators() {
        Loop loop = new Loop(new Separators('a', 'b', 'c'), "ST");
        Separators separators = new Separators('~', '*', ':');
        loop.setSeparators(separators);
        Assert.assertEquals("[~,*,:]", loop.getSeparators().toString());
    }

    @Test
    public void testSetChildIntString() {
        Loop loop = new Loop("X12");
        loop.addLoop("ISA");
        loop.addLoop("GS");
        loop.addLoop("XX");
        loop.addLoop("1000A");
        loop.addLoop("1000B");
        loop.addLoop("2000");
        loop.addLoop("2100");
        loop.addLoop("2110");
        loop.addLoop("GE");
        loop.addLoop("IEA");
        loop.setLoop(2, "ST"); // test
        Assert.assertEquals("ST", loop.getLoop(2).getId());
    }

    @Test
    public void testSetChildIntLoop() {
        Loop loop = new Loop("X12");
        loop.addLoop("ISA");
        loop.addLoop("GS");
        loop.addLoop("XX");
        loop.addLoop("1000A");
        loop.addLoop("1000B");
        loop.addLoop("2000");
        loop.addLoop("2100");
        loop.addLoop("2110");
        loop.addLoop("GE");
        loop.addLoop("IEA");
        loop.setLoop(2, new Loop("ST"));
        Assert.assertEquals("ST", loop.getLoop(2).getId());
    }

    @Test
    public void testSetSegmentInt() {
        Loop loop = new Loop("ST");
        loop.addSegment("NOT*THE*RIGHT*SEGMENT");
        loop.addSegment("BPR*DATA*NOT*VALID*RANDOM*TEXT");
        loop.addSegment("TRN*1*0000000000*1999999999");
        loop.addSegment("DTM*111*20090915");
        Segment segment = new Segment(new Separators('~', '*', ':'));
        segment.addElements("ST*835*000000001");
        loop.setSegment(0, segment);
        Assert.assertEquals("ST", loop.getSegment(0).getId());
    }

    @Test
    public void testSetSegmentIntSegment() {
        Loop loop = new Loop("ST");
        loop.addSegment("ST*835*000000001");
        loop.addSegment("BPR*DATA*NOT*VALID*RANDOM*TEXT");
        loop.addSegment("DTM*111*20090915");
        loop.addSegment("NOT*THE*RIGHT*SEGMENT");
        loop.setSegment(2, "TRN*1*0000000000*1999999999");
        Assert.assertEquals("TRN", loop.getSegment(2).getId());
    }

    @Test
    public void testSetName() {
        Loop loop = new Loop("AB");
        loop.setId("ST");
        Assert.assertEquals("ST", loop.getId());
    }

    @Test
    public void testToString() {
        Loop loop = new Loop("ST");
        loop.addSegment("ST*835*000000001");
        Assert.assertEquals("ST*835*000000001~", loop.toString());
    }

    @Test
    public void testToXML() {
        Loop loop = new Loop("ST");
        loop.addSegment("ST*835*000000001");
        Assert.assertEquals("<loop id=\"ST\">\n"
                + "  <segments>\n"
                + "    <segment id=\"ST\">\n"
                + "      <elements>\n"
                + "        <element id=\"ST01\">835</element>\n"
                + "        <element id=\"ST02\">000000001</element>\n"
                + "      </elements>\n"
                + "    </segment>\n"
                + "  </segments>\n"
                + "  <loops/>\n"
                + "</loop>", loop.toXML());
    }

}
