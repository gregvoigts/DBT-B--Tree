package de.tuberlin.dima.dbt.exercises.bplustree;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static de.tuberlin.dima.dbt.grading.bplustree.BPlusTreeMatcher.isTree;
import static de.tuberlin.dima.dbt.exercises.bplustree.BPlusTreeUtilities.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class BPlusTreeTest {

        // fail each test after 1 second
        // @Rule
        // public Timeout globalTimeout = new Timeout(1000);

        private BPlusTree tree, tree2, tree3;

        ///// Lookup tests

        @Test
        public void findKeyInLeaf() {
                // given
                tree = newTree(newLeaf(keys(1, 2, 3), values("a", "b", "c")));
                // when
                String value = tree.lookup(2);
                // then
                assertThat(value, is("b"));
        }

        @Test
        public void findNoKeyInLeaf() {
                // given
                tree = newTree(newLeaf(keys(1, 3), values("a", "c")));
                // when
                String value = tree.lookup(2);
                // then
                assertThat(value, is(nullValue()));
        }

        @Test
        public void findKeyInChild() {
                // given
                tree = newTree(newNode(keys(3),
                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4), values("c", "d")))));
                // when
                String value = tree.lookup(1);
                // then
                assertThat(value, is("a"));
        }

        @Test
        public void findNoKeyInChild() {
                // given
                tree = newTree(newNode(keys(3),
                                nodes(newLeaf(keys(1, 3), values("a", "c")),
                                                newLeaf(keys(5, 7), values("e", "g")))));
                // when
                String value = tree.lookup(6);
                // then
                assertThat(value, is(nullValue()));
        }

        @Test
        public void findKeyInGrandchild() {
                // given
                tree = newTree(newNode(keys(9, 20),
                                nodes(newNode(keys(3, 6),
                                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                                newLeaf(keys(3, 4), values("c", "d")),
                                                                newLeaf(keys(6, 7), values("e", "f")))),
                                                newNode(keys(12, 16),
                                                                nodes(newLeaf(keys(9, 10), values("g", "h")),
                                                                                newLeaf(keys(12, 14), values("i", "j")),
                                                                                newLeaf(keys(16, 17),
                                                                                                values("k", "l")))),
                                                newNode(keys(25, 28),
                                                                nodes(newLeaf(keys(20, 22), values("m", "n")),
                                                                                newLeaf(keys(25, 27), values("o", "p")),
                                                                                newLeaf(keys(28, 29),
                                                                                                values("q", "r")))))));
                // when
                String value = tree.lookup(7);
                // then
                assertThat(value, is("f"));
        }

        @Test
        public void findNoKeyInGrandchild() {
                // given
                tree = newTree(newNode(keys(9, 20),
                                nodes(newNode(keys(3, 6),
                                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                                newLeaf(keys(3, 4), values("c", "d")),
                                                                newLeaf(keys(6, 7), values("e", "f")))),
                                                newNode(keys(12, 16),
                                                                nodes(newLeaf(keys(9, 10), values("g", "h")),
                                                                                newLeaf(keys(12, 14), values("i", "j")),
                                                                                newLeaf(keys(16, 17),
                                                                                                values("k", "l")))),
                                                newNode(keys(25, 28),
                                                                nodes(newLeaf(keys(20, 22), values("m", "n")),
                                                                                newLeaf(keys(25, 27), values("o", "p")),
                                                                                newLeaf(keys(28, 29),
                                                                                                values("q", "r")))))));
                // when
                String value = tree.lookup(21);
                // then
                assertThat(value, is(nullValue()));
        }

        ///// Insertion tests

        @Test
        public void insertIntoLeaf() {
                // given
                tree = newTree(newLeaf(keys(1, 3), values("a", "c")));
                // when
                tree.insert(2, "b");
                // then
                assertThat(tree, isTree(
                                newTree(newLeaf(keys(1, 2, 3), values("a", "b", "c")))));
        }

        @Test
        public void insertIntoChild() {
                // given
                tree = newTree(newNode(keys(4),
                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(4, 5, 7),
                                                                values("c", "d", "f")))));
                // when
                tree.insert(3, "g");
                // then
                assertThat(tree, isTree(newTree(newNode(keys(4),
                                nodes(newLeaf(keys(1, 2, 3), values("a", "b", "g")),
                                                newLeaf(keys(4, 5, 7),
                                                                values("c", "d", "f")))))));
        }

        @Test
        public void splitLeafs() {
                // given
                tree = newTree(newNode(keys(3),
                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4, 5, 6),
                                                                values("c", "d", "e", "f")))));
                // when
                tree.insert(7, "g");
                // then
                assertThat(tree, isTree(newTree(newNode(
                                keys(3, 5),
                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4), values("c", "d")),
                                                newLeaf(keys(5, 6, 7), values("e", "f", "g")))))));
        }

        ///// Deletion tests

        @Test
        public void deleteFromRoot() {
                // given
                tree = newTree(newLeaf(keys(1, 2), values("a", "b")));
                // when
                String value = tree.delete(2);
                // then
                assertThat(value, is("b"));
                assertThat(tree, isTree(
                                newTree(newLeaf(keys(1), values("a")))));
        }

        @Test
        public void deleteFromLeaf() {
                // given
                tree = newTree(newLeaf(keys(1, 2, 3), values("a", "b", "c")));
                // when
                String value = tree.delete(2);
                // then
                assertThat(value, is("b"));
                assertThat(tree, isTree(
                                newTree(newLeaf(keys(1, 3), values("a", "c")))));
        }

        @Test
        public void deleteFromChild() {
                // given
                tree = newTree(newNode(
                                keys(4), nodes(newLeaf(keys(1, 2, 3), values("a", "b", "c")),
                                                newLeaf(keys(4, 5), values("d", "e")))));
                // when
                String value = tree.delete(1);
                // then
                assertThat(value, is("a"));
                assertThat(tree, isTree(newTree(newNode(
                                keys(4), nodes(newLeaf(keys(2, 3), values("b", "c")),
                                                newLeaf(keys(4, 5), values("d", "e")))))));
        }

        @Test
        public void deleteFromChildStealFromRightSibling() {
                // given
                tree = newTree(newNode(
                                keys(3), nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4, 5), values("c", "d", "e")))));
                // when
                String value = tree.delete(1);
                // then
                assertThat(value, is("a"));
                assertThat(tree, isTree(newTree(newNode(
                                keys(4), nodes(newLeaf(keys(2, 3), values("b", "c")),
                                                newLeaf(keys(4, 5), values("d", "e")))))));

        }

        @Test
        public void deleteFromChildStealFromLeftSibling() {
                // given
                tree = newTree(newNode(
                                keys(4), nodes(newLeaf(keys(1, 2, 3), values("a", "b", "c")),
                                                newLeaf(keys(4, 5), values("d", "e")))));
                // when
                String value = tree.delete(4);
                // then
                assertThat(value, is("d"));
                assertThat(tree, isTree(newTree(newNode(
                                keys(3), nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 5), values("c", "e")))))));

        }

        @Test
        public void deleteFromChildStealFromLeftSibling2() {
                // given
                tree = newTree(newNode(
                                keys(4), nodes(newLeaf(keys(1, 2, 3), values("a", "b", "c")),
                                                newLeaf(keys(4, 5), values("d", "e")))));
                // when
                String value = tree.delete(5);
                // then
                assertThat(value, is("e"));
                assertThat(tree, isTree(newTree(newNode(
                                keys(3), nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4), values("c", "d")))))));

        }

        @Test
        public void deleteFromChildMergeWithSibling() {
                // given
                tree = newTree(newNode(keys(3, 5),
                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4), values("c", "d")),
                                                newLeaf(keys(5, 6), values("e", "f")))));
                // when
                String value = tree.delete(2);
                // then
                assertThat(value, is("b"));
                assertThat(tree, isTree(newTree(newNode(
                                keys(5), nodes(newLeaf(keys(1, 3, 4), values("a", "c", "d")),
                                                newLeaf(keys(5, 6), values("e", "f")))))));
        }

        @Test
        public void deleteFromChildMergeWithSiblingOneLeft() {
                // given
                tree = newTree(newNode(keys(3),
                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4), values("c", "d")))));
                // when
                String value = tree.delete(2);
                // then
                assertThat(value, is("b"));
                assertThat(tree, isTree(newTree(newLeaf(keys(1, 3, 4), values("a", "c", "d")))));
        }

        @Test
        public void deleteFromChildMergeWithSiblingFirstRight() {
                // given
                tree = newTree(newNode(keys(3, 5),
                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4), values("c", "d")),
                                                newLeaf(keys(5, 6), values("e", "f")))));
                // when
                String value = tree.delete(3);
                // then
                assertThat(value, is("c"));
                assertThat(tree, isTree(newTree(newNode(
                                keys(3), nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(4, 5, 6), values("d", "e", "f")))))));
        }

        @Test
        public void deleteFromChildMergeWithSiblinRightNotAvaileble() {
                // given
                tree = newTree(newNode(keys(3, 5),
                                nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4), values("c", "d")),
                                                newLeaf(keys(5, 6), values("e", "f")))));
                // when
                String value = tree.delete(5);
                // then
                assertThat(value, is("e"));
                assertThat(tree, isTree(newTree(newNode(
                                keys(3), nodes(newLeaf(keys(1, 2), values("a", "b")),
                                                newLeaf(keys(3, 4, 6), values("c", "d", "f")))))));
        }

        // Combi
        @Test
        public void insertWithFullParentNodes() {
                // given

                // complete tree full
                tree = newTree(newNode(
                                keys(70, 130, 189, 250), nodes(
                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                newLeaf(keys(1, 2, 3, 4), values("a", "b", "c", "d")),
                                                                newLeaf(keys(10, 12, 18, 22),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(25, 27, 31, 34),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(40, 43, 45, 48),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(50, 54, 60, 64),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(keys(80, 91, 99, 112), nodes(
                                                                newLeaf(keys(70, 72, 74, 76),
                                                                                values("a", "b", "c", "d")),
                                                                newLeaf(keys(80, 83, 85, 89),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(91, 93, 95, 97),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(99, 103, 107, 110),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(112, 118, 124, 126),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(
                                                                keys(144, 152, 163, 175), nodes(
                                                                                newLeaf(keys(130, 134, 140, 142),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(144, 146, 148, 150),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(152, 155, 158, 160),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(163, 165, 170, 173),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(175, 178, 180, 185),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                newNode(
                                                                keys(198, 206, 216, 235), nodes(
                                                                                newLeaf(keys(189, 191, 194, 196),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(198, 200, 202, 204),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(206, 209, 211, 213),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(216, 220, 225, 230),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(235, 240, 242, 244),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                newNode(
                                                                keys(270, 285, 293, 302), nodes(
                                                                                newLeaf(keys(250, 255, 260, 265),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(270, 276, 280, 282),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(285, 287, 289, 291),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(293, 295, 297, 299),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(302, 304, 306, 310),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))))));
                // Root has one place left
                tree2 = newTree(newNode(
                                keys(70, 130, 189), nodes(
                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                newLeaf(keys(1, 2, 3, 4), values("a", "b", "c", "d")),
                                                                newLeaf(keys(10, 12, 18, 22),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(25, 27, 31, 34),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(40, 43, 45, 48),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(50, 54, 60, 64),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(keys(80, 91, 99, 112), nodes(
                                                                newLeaf(keys(70, 72, 74, 76),
                                                                                values("a", "b", "c", "d")),
                                                                newLeaf(keys(80, 83, 85, 89),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(91, 93, 95, 97),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(99, 103, 107, 110),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(112, 118, 124, 126),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(
                                                                keys(144, 152, 163, 175), nodes(
                                                                                newLeaf(keys(130, 134, 140, 142),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(144, 146, 148, 150),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(152, 155, 158, 160),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(163, 165, 170, 173),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(175, 178, 180, 185),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                newNode(
                                                                keys(198, 206, 216, 235), nodes(
                                                                                newLeaf(keys(189, 191, 194, 196),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(198, 200, 202, 204),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(206, 209, 211, 213),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(216, 220, 225, 230),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(235, 240, 242, 244),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))))));
                tree3 = newTree(newNode(
                                keys(70, 130, 189), nodes(
                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                newLeaf(keys(1, 2, 3, 4), values("a", "b", "c", "d")),
                                                                newLeaf(keys(10, 12, 18, 22),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(25, 27, 31, 34),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(40, 43, 45, 48),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(50, 54, 60, 64),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(keys(80, 91, 99, 112), nodes(
                                                                newLeaf(keys(70, 72, 74, 76),
                                                                                values("a", "b", "c", "d")),
                                                                newLeaf(keys(80, 83, 85, 89),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(91, 93, 95, 97),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(99, 103, 107, 110),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(112, 118, 124, 126),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(
                                                                keys(144, 152, 163, 175), nodes(
                                                                                newLeaf(keys(130, 134, 140, 142),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(144, 146, 148, 150),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(152, 155, 158, 160),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(163, 165, 170, 173),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(175, 178, 180, 185),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                newNode(
                                                                keys(198, 206, 216, 235), nodes(
                                                                                newLeaf(keys(189, 191, 194, 196),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(198, 200, 202, 204),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(206, 209, 211, 213),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(216, 220, 225, 230),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(235, 240, 242, 244),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))))));

                // when
                tree2.insert(82, "test");
                tree.insert(82, "test");
                tree3.insert(0, "test");

                // then
                // assertThat(value, is("a"));
                assertThat(tree2, isTree(newTree(newNode(
                                keys(70, 91, 130, 189), nodes(
                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                newLeaf(keys(1, 2, 3, 4), values("a", "b", "c", "d")),
                                                                newLeaf(keys(10, 12, 18, 22),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(25, 27, 31, 34),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(40, 43, 45, 48),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(50, 54, 60, 64),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(keys(80, 83), nodes(
                                                                newLeaf(keys(70, 72, 74, 76),
                                                                                values("a", "b", "c", "d")),
                                                                newLeaf(keys(80, 82), values("e", "test")),
                                                                newLeaf(keys(83, 85, 89), values("f", "g", "h")))),
                                                newNode(
                                                                keys(99, 112), nodes(
                                                                                newLeaf(keys(91, 93, 95, 97),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(99, 103, 107, 110),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(112, 118, 124, 126),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                newNode(
                                                                keys(144, 152, 163, 175), nodes(
                                                                                newLeaf(keys(130, 134, 140, 142),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(144, 146, 148, 150),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(152, 155, 158, 160),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(163, 165, 170, 173),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(175, 178, 180, 185),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                newNode(
                                                                keys(198, 206, 216, 235), nodes(
                                                                                newLeaf(keys(189, 191, 194, 196),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(198, 200, 202, 204),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(206, 209, 211, 213),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(216, 220, 225, 230),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(235, 240, 242, 244),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))))))));

                assertThat(tree, isTree(newTree(
                                newNode(keys(130), nodes(
                                                newNode(
                                                                keys(70, 91), nodes(
                                                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                                                newLeaf(keys(1, 2, 3,
                                                                                                                4),
                                                                                                                values("a", "b", "c",
                                                                                                                                "d")),
                                                                                                newLeaf(keys(10, 12, 18,
                                                                                                                22),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(25, 27, 31,
                                                                                                                34),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(40, 43, 45,
                                                                                                                48),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(50, 54, 60,
                                                                                                                64),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")))),
                                                                                newNode(keys(80, 83), nodes(
                                                                                                newLeaf(keys(70, 72, 74,
                                                                                                                76),
                                                                                                                values("a", "b", "c",
                                                                                                                                "d")),
                                                                                                newLeaf(keys(80, 82),
                                                                                                                values("e", "test")),
                                                                                                newLeaf(keys(83, 85,
                                                                                                                89),
                                                                                                                values("f", "g", "h")))),
                                                                                newNode(
                                                                                                keys(99, 112), nodes(
                                                                                                                newLeaf(keys(91, 93,
                                                                                                                                95,
                                                                                                                                97),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(99, 103,
                                                                                                                                107,
                                                                                                                                110),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(112,
                                                                                                                                118,
                                                                                                                                124,
                                                                                                                                126),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))))),
                                                newNode(
                                                                keys(189, 250), nodes(
                                                                                newNode(
                                                                                                keys(144, 152, 163,
                                                                                                                175),
                                                                                                nodes(
                                                                                                                newLeaf(keys(130,
                                                                                                                                134,
                                                                                                                                140,
                                                                                                                                142),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(144,
                                                                                                                                146,
                                                                                                                                148,
                                                                                                                                150),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(152,
                                                                                                                                155,
                                                                                                                                158,
                                                                                                                                160),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(163,
                                                                                                                                165,
                                                                                                                                170,
                                                                                                                                173),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(175,
                                                                                                                                178,
                                                                                                                                180,
                                                                                                                                185),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(198, 206, 216,
                                                                                                                235),
                                                                                                nodes(
                                                                                                                newLeaf(keys(189,
                                                                                                                                191,
                                                                                                                                194,
                                                                                                                                196),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(198,
                                                                                                                                200,
                                                                                                                                202,
                                                                                                                                204),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(206,
                                                                                                                                209,
                                                                                                                                211,
                                                                                                                                213),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(216,
                                                                                                                                220,
                                                                                                                                225,
                                                                                                                                230),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(235,
                                                                                                                                240,
                                                                                                                                242,
                                                                                                                                244),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(270, 285, 293,
                                                                                                                302),
                                                                                                nodes(
                                                                                                                newLeaf(keys(250,
                                                                                                                                255,
                                                                                                                                260,
                                                                                                                                265),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(270,
                                                                                                                                276,
                                                                                                                                280,
                                                                                                                                282),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(285,
                                                                                                                                287,
                                                                                                                                289,
                                                                                                                                291),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(293,
                                                                                                                                295,
                                                                                                                                297,
                                                                                                                                299),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(302,
                                                                                                                                304,
                                                                                                                                306,
                                                                                                                                310),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h"))))))))

                )));
                assertThat(tree3, isTree(newTree(newNode(
                                keys(25, 70, 130, 189), nodes(
                                                newNode(keys(2, 10), nodes(
                                                                newLeaf(keys(0, 1), values("test", "a")),
                                                                newLeaf(keys(2, 3, 4), values("b", "c", "d")),
                                                                newLeaf(keys(10, 12, 18, 22),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(keys(40, 50), nodes(
                                                                newLeaf(keys(25, 27, 31, 34),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(40, 43, 45, 48),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(50, 54, 60, 64),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(keys(80, 91, 99, 112), nodes(
                                                                newLeaf(keys(70, 72, 74, 76),
                                                                                values("a", "b", "c", "d")),
                                                                newLeaf(keys(80, 83, 85, 89),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(91, 93, 95, 97),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(99, 103, 107, 110),
                                                                                values("e", "f", "g", "h")),
                                                                newLeaf(keys(112, 118, 124, 126),
                                                                                values("e", "f", "g", "h")))),
                                                newNode(
                                                                keys(144, 152, 163, 175), nodes(
                                                                                newLeaf(keys(130, 134, 140, 142),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(144, 146, 148, 150),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(152, 155, 158, 160),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(163, 165, 170, 173),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(175, 178, 180, 185),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                newNode(
                                                                keys(198, 206, 216, 235), nodes(
                                                                                newLeaf(keys(189, 191, 194, 196),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(198, 200, 202, 204),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(206, 209, 211, 213),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(216, 220, 225, 230),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(235, 240, 242, 244),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))))))));

                // insert value after node manipulation in previous test
                tree.insert(400, "test");

                assertThat(tree, isTree(newTree(
                                newNode(keys(130), nodes(
                                                newNode(
                                                                keys(70, 91), nodes(
                                                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                                                newLeaf(keys(1, 2, 3,
                                                                                                                4),
                                                                                                                values("a", "b", "c",
                                                                                                                                "d")),
                                                                                                newLeaf(keys(10, 12, 18,
                                                                                                                22),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(25, 27, 31,
                                                                                                                34),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(40, 43, 45,
                                                                                                                48),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(50, 54, 60,
                                                                                                                64),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")))),
                                                                                newNode(keys(80, 83), nodes(
                                                                                                newLeaf(keys(70, 72, 74,
                                                                                                                76),
                                                                                                                values("a", "b", "c",
                                                                                                                                "d")),
                                                                                                newLeaf(keys(80, 82),
                                                                                                                values("e", "test")),
                                                                                                newLeaf(keys(83, 85,
                                                                                                                89),
                                                                                                                values("f", "g", "h")))),
                                                                                newNode(
                                                                                                keys(99, 112), nodes(
                                                                                                                newLeaf(keys(91, 93,
                                                                                                                                95,
                                                                                                                                97),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(99, 103,
                                                                                                                                107,
                                                                                                                                110),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(112,
                                                                                                                                118,
                                                                                                                                124,
                                                                                                                                126),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))))),
                                                newNode(
                                                                keys(189, 250, 293), nodes(
                                                                                newNode(
                                                                                                keys(144, 152, 163,
                                                                                                                175),
                                                                                                nodes(
                                                                                                                newLeaf(keys(130,
                                                                                                                                134,
                                                                                                                                140,
                                                                                                                                142),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(144,
                                                                                                                                146,
                                                                                                                                148,
                                                                                                                                150),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(152,
                                                                                                                                155,
                                                                                                                                158,
                                                                                                                                160),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(163,
                                                                                                                                165,
                                                                                                                                170,
                                                                                                                                173),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(175,
                                                                                                                                178,
                                                                                                                                180,
                                                                                                                                185),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(198, 206, 216,
                                                                                                                235),
                                                                                                nodes(
                                                                                                                newLeaf(keys(189,
                                                                                                                                191,
                                                                                                                                194,
                                                                                                                                196),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(198,
                                                                                                                                200,
                                                                                                                                202,
                                                                                                                                204),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(206,
                                                                                                                                209,
                                                                                                                                211,
                                                                                                                                213),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(216,
                                                                                                                                220,
                                                                                                                                225,
                                                                                                                                230),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(235,
                                                                                                                                240,
                                                                                                                                242,
                                                                                                                                244),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(270, 285), nodes(
                                                                                                                newLeaf(keys(250,
                                                                                                                                255,
                                                                                                                                260,
                                                                                                                                265),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(270,
                                                                                                                                276,
                                                                                                                                280,
                                                                                                                                282),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(285,
                                                                                                                                287,
                                                                                                                                289,
                                                                                                                                291),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(302, 306), nodes(
                                                                                                                newLeaf(keys(293,
                                                                                                                                295,
                                                                                                                                297,
                                                                                                                                299),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(302,
                                                                                                                                304),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(306,
                                                                                                                                310,
                                                                                                                                400),
                                                                                                                                values("g", "h", "test"))))))))

                )));

                // insert value after node manipulation in previous test
                tree.insert(94, "test");
                assertThat(tree, isTree(newTree(
                                newNode(keys(130), nodes(
                                                newNode(
                                                                keys(70, 91), nodes(
                                                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                                                newLeaf(keys(1, 2, 3,
                                                                                                                4),
                                                                                                                values("a", "b", "c",
                                                                                                                                "d")),
                                                                                                newLeaf(keys(10, 12, 18,
                                                                                                                22),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(25, 27, 31,
                                                                                                                34),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(40, 43, 45,
                                                                                                                48),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(50, 54, 60,
                                                                                                                64),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")))),
                                                                                newNode(keys(80, 83), nodes(
                                                                                                newLeaf(keys(70, 72, 74,
                                                                                                                76),
                                                                                                                values("a", "b", "c",
                                                                                                                                "d")),
                                                                                                newLeaf(keys(80, 82),
                                                                                                                values("e", "test")),
                                                                                                newLeaf(keys(83, 85,
                                                                                                                89),
                                                                                                                values("f", "g", "h")))),
                                                                                newNode(
                                                                                                keys(94, 99, 112),
                                                                                                nodes(
                                                                                                                newLeaf(keys(91, 93),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(94, 95,
                                                                                                                                97),
                                                                                                                                values("test", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(99, 103,
                                                                                                                                107,
                                                                                                                                110),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(112,
                                                                                                                                118,
                                                                                                                                124,
                                                                                                                                126),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))))),
                                                newNode(
                                                                keys(189, 250, 293), nodes(
                                                                                newNode(
                                                                                                keys(144, 152, 163,
                                                                                                                175),
                                                                                                nodes(
                                                                                                                newLeaf(keys(130,
                                                                                                                                134,
                                                                                                                                140,
                                                                                                                                142),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(144,
                                                                                                                                146,
                                                                                                                                148,
                                                                                                                                150),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(152,
                                                                                                                                155,
                                                                                                                                158,
                                                                                                                                160),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(163,
                                                                                                                                165,
                                                                                                                                170,
                                                                                                                                173),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(175,
                                                                                                                                178,
                                                                                                                                180,
                                                                                                                                185),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(198, 206, 216,
                                                                                                                235),
                                                                                                nodes(
                                                                                                                newLeaf(keys(189,
                                                                                                                                191,
                                                                                                                                194,
                                                                                                                                196),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(198,
                                                                                                                                200,
                                                                                                                                202,
                                                                                                                                204),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(206,
                                                                                                                                209,
                                                                                                                                211,
                                                                                                                                213),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(216,
                                                                                                                                220,
                                                                                                                                225,
                                                                                                                                230),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(235,
                                                                                                                                240,
                                                                                                                                242,
                                                                                                                                244),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(270, 285), nodes(
                                                                                                                newLeaf(keys(250,
                                                                                                                                255,
                                                                                                                                260,
                                                                                                                                265),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(270,
                                                                                                                                276,
                                                                                                                                280,
                                                                                                                                282),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(285,
                                                                                                                                287,
                                                                                                                                289,
                                                                                                                                291),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(302, 306), nodes(
                                                                                                                newLeaf(keys(293,
                                                                                                                                295,
                                                                                                                                297,
                                                                                                                                299),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(302,
                                                                                                                                304),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(306,
                                                                                                                                310,
                                                                                                                                400),
                                                                                                                                values("g", "h", "test"))))))))

                )));
                // cause new node on hight 3 ->preparation for next test (new node height 3)
                tree.insert(174, "test");
                assertThat(tree, isTree(newTree(
                                newNode(keys(130), nodes(
                                                newNode(
                                                                keys(70, 91), nodes(
                                                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                                                newLeaf(keys(1, 2, 3,
                                                                                                                4),
                                                                                                                values("a", "b", "c",
                                                                                                                                "d")),
                                                                                                newLeaf(keys(10, 12, 18,
                                                                                                                22),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(25, 27, 31,
                                                                                                                34),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(40, 43, 45,
                                                                                                                48),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(50, 54, 60,
                                                                                                                64),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")))),
                                                                                newNode(keys(80, 83), nodes(
                                                                                                newLeaf(keys(70, 72, 74,
                                                                                                                76),
                                                                                                                values("a", "b", "c",
                                                                                                                                "d")),
                                                                                                newLeaf(keys(80, 82),
                                                                                                                values("e", "test")),
                                                                                                newLeaf(keys(83, 85,
                                                                                                                89),
                                                                                                                values("f", "g", "h")))),
                                                                                newNode(
                                                                                                keys(94, 99, 112),
                                                                                                nodes(
                                                                                                                newLeaf(keys(91, 93),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(94, 95,
                                                                                                                                97),
                                                                                                                                values("test", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(99, 103,
                                                                                                                                107,
                                                                                                                                110),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(112,
                                                                                                                                118,
                                                                                                                                124,
                                                                                                                                126),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))))),
                                                newNode(
                                                                keys(163, 189, 250, 293), nodes(
                                                                                newNode(
                                                                                                keys(144, 152), nodes(
                                                                                                                newLeaf(keys(130,
                                                                                                                                134,
                                                                                                                                140,
                                                                                                                                142),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(144,
                                                                                                                                146,
                                                                                                                                148,
                                                                                                                                150),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(152,
                                                                                                                                155,
                                                                                                                                158,
                                                                                                                                160),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(170, 175), nodes(
                                                                                                                newLeaf(keys(163,
                                                                                                                                165),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(170,
                                                                                                                                173,
                                                                                                                                174),
                                                                                                                                values("g", "h", "test")),
                                                                                                                newLeaf(keys(175,
                                                                                                                                178,
                                                                                                                                180,
                                                                                                                                185),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(198, 206, 216,
                                                                                                                235),
                                                                                                nodes(
                                                                                                                newLeaf(keys(189,
                                                                                                                                191,
                                                                                                                                194,
                                                                                                                                196),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(198,
                                                                                                                                200,
                                                                                                                                202,
                                                                                                                                204),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(206,
                                                                                                                                209,
                                                                                                                                211,
                                                                                                                                213),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(216,
                                                                                                                                220,
                                                                                                                                225,
                                                                                                                                230),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(235,
                                                                                                                                240,
                                                                                                                                242,
                                                                                                                                244),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(270, 285), nodes(
                                                                                                                newLeaf(keys(250,
                                                                                                                                255,
                                                                                                                                260,
                                                                                                                                265),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(270,
                                                                                                                                276,
                                                                                                                                280,
                                                                                                                                282),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(285,
                                                                                                                                287,
                                                                                                                                289,
                                                                                                                                291),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(302, 306), nodes(
                                                                                                                newLeaf(keys(293,
                                                                                                                                295,
                                                                                                                                297,
                                                                                                                                299),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(302,
                                                                                                                                304),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(306,
                                                                                                                                310,
                                                                                                                                400),
                                                                                                                                values("g", "h", "test"))))))))

                )));
                // cause new node on hight 3
                tree.insert(190, "test");
                assertThat(tree, isTree(newTree(
                                newNode(keys(130, 206), nodes(
                                                newNode(keys(70, 91), nodes(
                                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                                newLeaf(keys(1, 2, 3, 4),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(10, 12, 18, 22),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(25, 27, 31, 34),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(40, 43, 45, 48),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(50, 54, 60, 64),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                                newNode(keys(80, 83), nodes(
                                                                                newLeaf(keys(70, 72, 74, 76),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(80, 82),
                                                                                                values("e", "test")),
                                                                                newLeaf(keys(83, 85, 89), values("f",
                                                                                                "g", "h")))),
                                                                newNode(
                                                                                keys(94, 99, 112), nodes(
                                                                                                newLeaf(keys(91, 93),
                                                                                                                values("e", "f")),
                                                                                                newLeaf(keys(94, 95,
                                                                                                                97),
                                                                                                                values("test", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(99, 103,
                                                                                                                107,
                                                                                                                110),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(112, 118,
                                                                                                                124,
                                                                                                                126),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")))))),
                                                newNode(
                                                                keys(163, 189), nodes(
                                                                                newNode(
                                                                                                keys(144, 152), nodes(
                                                                                                                newLeaf(keys(130,
                                                                                                                                134,
                                                                                                                                140,
                                                                                                                                142),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(144,
                                                                                                                                146,
                                                                                                                                148,
                                                                                                                                150),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(152,
                                                                                                                                155,
                                                                                                                                158,
                                                                                                                                160),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(170, 175), nodes(
                                                                                                                newLeaf(keys(163,
                                                                                                                                165),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(170,
                                                                                                                                173,
                                                                                                                                174),
                                                                                                                                values("g", "h", "test")),
                                                                                                                newLeaf(keys(175,
                                                                                                                                178,
                                                                                                                                180,
                                                                                                                                185),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(191, 198), nodes(
                                                                                                                newLeaf(keys(189,
                                                                                                                                190),
                                                                                                                                values("a", "test")),
                                                                                                                newLeaf(keys(191,
                                                                                                                                194,
                                                                                                                                196),
                                                                                                                                values("b", "c", "d")),
                                                                                                                newLeaf(keys(198,
                                                                                                                                200,
                                                                                                                                202,
                                                                                                                                204),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ))),
                                                newNode(
                                                                keys(250, 293), nodes(
                                                                                newNode(
                                                                                                keys(216, 235), nodes(
                                                                                                                newLeaf(keys(206,
                                                                                                                                209,
                                                                                                                                211,
                                                                                                                                213),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(216,
                                                                                                                                220,
                                                                                                                                225,
                                                                                                                                230),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(235,
                                                                                                                                240,
                                                                                                                                242,
                                                                                                                                244),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(270, 285), nodes(
                                                                                                                newLeaf(keys(250,
                                                                                                                                255,
                                                                                                                                260,
                                                                                                                                265),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(270,
                                                                                                                                276,
                                                                                                                                280,
                                                                                                                                282),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(285,
                                                                                                                                287,
                                                                                                                                289,
                                                                                                                                291),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(302, 306), nodes(
                                                                                                                newLeaf(keys(293,
                                                                                                                                295,
                                                                                                                                297,
                                                                                                                                299),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(302,
                                                                                                                                304),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(306,
                                                                                                                                310,
                                                                                                                                400),
                                                                                                                                values("g", "h", "test"))))))))

                )));

                // Some lookup Tests

                // Lookup inserted value
                String value = tree.lookup(400);
                assertThat(value, is("test"));

                // Lookup inserted value
                value = tree.lookup(80);
                assertThat(value, is("e"));

                value = tree.lookup(500);
                assertThat(value, is(nullValue()));

                value = tree.lookup(131);
                assertThat(value, is(nullValue()));

                value = tree.lookup(0);
                assertThat(value, is(nullValue()));

                // Some delete tests

                // here in (height>2) possible because no key rotation
                value = tree.delete(400);

                assertThat(value, is("test"));
                assertThat(tree, isTree(newTree(
                                newNode(keys(130, 206), nodes(
                                                newNode(keys(70, 91), nodes(
                                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                                newLeaf(keys(1, 2, 3, 4),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(10, 12, 18, 22),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(25, 27, 31, 34),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(40, 43, 45, 48),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(50, 54, 60, 64),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                                newNode(keys(80, 83), nodes(
                                                                                newLeaf(keys(70, 72, 74, 76),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(80, 82),
                                                                                                values("e", "test")),
                                                                                newLeaf(keys(83, 85, 89), values("f",
                                                                                                "g", "h")))),
                                                                newNode(
                                                                                keys(94, 99, 112), nodes(
                                                                                                newLeaf(keys(91, 93),
                                                                                                                values("e", "f")),
                                                                                                newLeaf(keys(94, 95,
                                                                                                                97),
                                                                                                                values("test", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(99, 103,
                                                                                                                107,
                                                                                                                110),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(112, 118,
                                                                                                                124,
                                                                                                                126),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")))))),
                                                newNode(
                                                                keys(163, 189), nodes(
                                                                                newNode(
                                                                                                keys(144, 152), nodes(
                                                                                                                newLeaf(keys(130,
                                                                                                                                134,
                                                                                                                                140,
                                                                                                                                142),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(144,
                                                                                                                                146,
                                                                                                                                148,
                                                                                                                                150),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(152,
                                                                                                                                155,
                                                                                                                                158,
                                                                                                                                160),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(170, 175), nodes(
                                                                                                                newLeaf(keys(163,
                                                                                                                                165),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(170,
                                                                                                                                173,
                                                                                                                                174),
                                                                                                                                values("g", "h", "test")),
                                                                                                                newLeaf(keys(175,
                                                                                                                                178,
                                                                                                                                180,
                                                                                                                                185),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(191, 198), nodes(
                                                                                                                newLeaf(keys(189,
                                                                                                                                190),
                                                                                                                                values("a", "test")),
                                                                                                                newLeaf(keys(191,
                                                                                                                                194,
                                                                                                                                196),
                                                                                                                                values("b", "c", "d")),
                                                                                                                newLeaf(keys(198,
                                                                                                                                200,
                                                                                                                                202,
                                                                                                                                204),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ))),
                                                newNode(
                                                                keys(250, 293), nodes(
                                                                                newNode(
                                                                                                keys(216, 235), nodes(
                                                                                                                newLeaf(keys(206,
                                                                                                                                209,
                                                                                                                                211,
                                                                                                                                213),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(216,
                                                                                                                                220,
                                                                                                                                225,
                                                                                                                                230),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(235,
                                                                                                                                240,
                                                                                                                                242,
                                                                                                                                244),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(270, 285), nodes(
                                                                                                                newLeaf(keys(250,
                                                                                                                                255,
                                                                                                                                260,
                                                                                                                                265),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(270,
                                                                                                                                276,
                                                                                                                                280,
                                                                                                                                282),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(285,
                                                                                                                                287,
                                                                                                                                289,
                                                                                                                                291),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(302, 306), nodes(
                                                                                                                newLeaf(keys(293,
                                                                                                                                295,
                                                                                                                                297,
                                                                                                                                299),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(302,
                                                                                                                                304),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(306,
                                                                                                                                310),
                                                                                                                                values("g", "h"))))))))

                )));

                value = tree.lookup(400);
                assertThat(value, is(nullValue()));

                // insert again
                tree.insert(400, "test_new");
                value = tree.lookup(400);
                assertThat(value, is("test_new"));
                assertThat(tree, isTree(newTree(
                                newNode(keys(130, 206), nodes(
                                                newNode(keys(70, 91), nodes(
                                                                newNode(keys(10, 25, 40, 50), nodes(
                                                                                newLeaf(keys(1, 2, 3, 4),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(10, 12, 18, 22),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(25, 27, 31, 34),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(40, 43, 45, 48),
                                                                                                values("e", "f", "g",
                                                                                                                "h")),
                                                                                newLeaf(keys(50, 54, 60, 64),
                                                                                                values("e", "f", "g",
                                                                                                                "h")))),
                                                                newNode(keys(80, 83), nodes(
                                                                                newLeaf(keys(70, 72, 74, 76),
                                                                                                values("a", "b", "c",
                                                                                                                "d")),
                                                                                newLeaf(keys(80, 82),
                                                                                                values("e", "test")),
                                                                                newLeaf(keys(83, 85, 89), values("f",
                                                                                                "g", "h")))),
                                                                newNode(
                                                                                keys(94, 99, 112), nodes(
                                                                                                newLeaf(keys(91, 93),
                                                                                                                values("e", "f")),
                                                                                                newLeaf(keys(94, 95,
                                                                                                                97),
                                                                                                                values("test", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(99, 103,
                                                                                                                107,
                                                                                                                110),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")),
                                                                                                newLeaf(keys(112, 118,
                                                                                                                124,
                                                                                                                126),
                                                                                                                values("e", "f", "g",
                                                                                                                                "h")))))),
                                                newNode(
                                                                keys(163, 189), nodes(
                                                                                newNode(
                                                                                                keys(144, 152), nodes(
                                                                                                                newLeaf(keys(130,
                                                                                                                                134,
                                                                                                                                140,
                                                                                                                                142),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(144,
                                                                                                                                146,
                                                                                                                                148,
                                                                                                                                150),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(152,
                                                                                                                                155,
                                                                                                                                158,
                                                                                                                                160),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(170, 175), nodes(
                                                                                                                newLeaf(keys(163,
                                                                                                                                165),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(170,
                                                                                                                                173,
                                                                                                                                174),
                                                                                                                                values("g", "h", "test")),
                                                                                                                newLeaf(keys(175,
                                                                                                                                178,
                                                                                                                                180,
                                                                                                                                185),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(191, 198), nodes(
                                                                                                                newLeaf(keys(189,
                                                                                                                                190),
                                                                                                                                values("a", "test")),
                                                                                                                newLeaf(keys(191,
                                                                                                                                194,
                                                                                                                                196),
                                                                                                                                values("b", "c", "d")),
                                                                                                                newLeaf(keys(198,
                                                                                                                                200,
                                                                                                                                202,
                                                                                                                                204),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ))),
                                                newNode(
                                                                keys(250, 293), nodes(
                                                                                newNode(
                                                                                                keys(216, 235), nodes(
                                                                                                                newLeaf(keys(206,
                                                                                                                                209,
                                                                                                                                211,
                                                                                                                                213),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(216,
                                                                                                                                220,
                                                                                                                                225,
                                                                                                                                230),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(235,
                                                                                                                                240,
                                                                                                                                242,
                                                                                                                                244),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))),
                                                                                newNode(
                                                                                                keys(270, 285), nodes(
                                                                                                                newLeaf(keys(250,
                                                                                                                                255,
                                                                                                                                260,
                                                                                                                                265),
                                                                                                                                values("a", "b", "c",
                                                                                                                                                "d")),
                                                                                                                newLeaf(keys(270,
                                                                                                                                276,
                                                                                                                                280,
                                                                                                                                282),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(285,
                                                                                                                                287,
                                                                                                                                289,
                                                                                                                                291),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")))

                                                                                ),
                                                                                newNode(
                                                                                                keys(302, 306), nodes(
                                                                                                                newLeaf(keys(293,
                                                                                                                                295,
                                                                                                                                297,
                                                                                                                                299),
                                                                                                                                values("e", "f", "g",
                                                                                                                                                "h")),
                                                                                                                newLeaf(keys(302,
                                                                                                                                304),
                                                                                                                                values("e", "f")),
                                                                                                                newLeaf(keys(306,
                                                                                                                                310,
                                                                                                                                400),
                                                                                                                                values("g", "h", "test_new"))))))))

                )));

        }

}
