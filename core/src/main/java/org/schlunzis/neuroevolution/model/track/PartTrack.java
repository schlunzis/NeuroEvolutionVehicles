package org.schlunzis.neuroevolution.model.track;

import org.schlunzis.neuroevolution.sdk.track.Track;
import org.schlunzis.neuroevolution.sdk.util.Boundary;
import org.schlunzis.neuroevolution.sdk.util.PVector;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Til7701
 */
public class PartTrack implements Track {

    private static final PVector VECTOR_TOP_LEFT = new PVector(0, 0);
    private static final PVector VECTOR_TOP_RIGHT = new PVector(1, 0);
    private static final PVector VECTOR_BOTTOM_LEFT = new PVector(0, 1);
    private static final PVector VECTOR_BOTTOM_RIGHT = new PVector(1, 1);

    private static final Boundary BOUNDARY_TOP = new Boundary(VECTOR_TOP_LEFT, VECTOR_TOP_RIGHT);
    private static final Boundary BOUNDARY_LEFT = new Boundary(VECTOR_TOP_LEFT, VECTOR_BOTTOM_LEFT);
    private static final Boundary BOUNDARY_RIGHT = new Boundary(VECTOR_TOP_RIGHT, VECTOR_BOTTOM_RIGHT);
    private static final Boundary BOUNDARY_BOTTOM = new Boundary(VECTOR_BOTTOM_LEFT, VECTOR_BOTTOM_RIGHT);
    private static final Boundary BOUNDARY_TOP_LEFT_BOTTOM_RIGHT = new Boundary(VECTOR_TOP_LEFT, VECTOR_BOTTOM_RIGHT);
    private static final Boundary BOUNDARY_TOP_RIGHT_BOTTOM_LEFT = new Boundary(VECTOR_TOP_RIGHT, VECTOR_BOTTOM_LEFT);

    private static final Boundary CHECKPOINT_VERTICAL = new Boundary(new PVector(0.5, 0), new PVector(0.5, 1));
    private static final Boundary CHECKPOINT_HORIZONTAL = new Boundary(new PVector(0, 0.5), new PVector(1, 0.5));
    private static final Boundary CHECKPOINT_TOP_LEFT = new Boundary(new PVector(0.5, 0.5), new PVector(0, 0));
    private static final Boundary CHECKPOINT_TOP_RIGHT = new Boundary(new PVector(0.5, 0.5), new PVector(1, 0));
    private static final Boundary CHECKPOINT_BOTTOM_LEFT = new Boundary(new PVector(0.5, 0.5), new PVector(0, 1));
    private static final Boundary CHECKPOINT_BOTTOM_RIGHT = new Boundary(new PVector(0.5, 0.5), new PVector(1, 1));


    private static final int ROWS = 10;
    private static final int COLUMNS = 10;

    private final Random random;
    private PART[][] track;

    @Getter
    private PVector start;

    @Getter
    private List<Boundary> walls;
    @Getter
    private List<Boundary> checkpoints;

    public String getTrackName(){
        return "PartTrack";
    }

    protected PartTrack() {
        random = new Random();
    }

    protected PartTrack(int seed) {
        random = new Random(seed);
    }

    @Override
    public void buildTrack() {
        walls = new ArrayList<>();
        checkpoints = new ArrayList<>();
        track = new PART[ROWS][COLUMNS];
        for (int i = COLUMNS - 2; i > 0; i--) {
            track[ROWS - 1][i] = PART.RIGHT_LEFT;
            generateWallsAndCheckpoints(ROWS - 1, i);
        }
        track[ROWS - 1][0] = PART.RIGHT_TOP;
        generateWallsAndCheckpoints(ROWS - 1, 0);

        try {
            generateTrack();
        } catch (Exception e) {
            e.printStackTrace();
        }

        track[ROWS - 1][COLUMNS - 1] = PART.TOP_LEFT;
        generateWallsAndCheckpoints(ROWS - 1, COLUMNS - 1);

        start = checkpoints.get(0).midPoint();
    }

    private void generateTrack() {
        int startRow = ROWS - 2;
        int startCol = 0;
        int row = startRow;
        int col = startCol;
        DIRECTION lastOut = DIRECTION.TOP;
        int turnOffset = 0;

        do {
            List<PART> availableParts = new ArrayList<>();
            try {
                availableParts = getAvailableParts(row, col, lastOut, turnOffset);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (availableParts.isEmpty()) {
                throw new IllegalStateException("No available parts");
            }
            PART part = availableParts.get(random.nextInt(availableParts.size()));
            track[row][col] = part;

            generateWallsAndCheckpoints(row, col);

            if (part.getTurn() == DIRECTION.LEFT) {
                turnOffset--;
            } else if (part.getTurn() == DIRECTION.RIGHT) {
                turnOffset++;
            }

            lastOut = part.getOut();

            int[] nextRowCol = getNextPosition(row, col, lastOut);
            row = nextRowCol[0];
            col = nextRowCol[1];
        } while (row != ROWS - 1 || col != COLUMNS - 1);
    }

    private List<PART> getAvailableParts(int row, int col, DIRECTION direction, int turnOffset) {
        List<PART> availableParts = PART.getPartsWithIn(DIRECTION.getOpposite(direction));

        removeEdgeParts(availableParts, row, col);
        removePartsBlockedByOtherParts(availableParts, row, col);

        if (turnOffset > 1) {
            availableParts.removeIf(part -> part.getTurn() == DIRECTION.RIGHT);
        } else if (turnOffset < 0) {
            availableParts.removeIf(part -> part.getTurn() == DIRECTION.LEFT);
        }

        if (row == 0) {
            availableParts.removeIf(part -> part.getOut() == DIRECTION.TOP);
            availableParts.removeIf(part -> part.getOut() == DIRECTION.LEFT);
        }

        if (col >= COLUMNS - 2) {
            availableParts.removeIf(part -> part.getOut() == DIRECTION.TOP);
        }

        return availableParts;
    }

    private void removeEdgeParts(List<PART> availableParts, int row, int col) {
        if (row == 0) {
            availableParts.removeIf(part -> part.getOut().equals(DIRECTION.TOP));
        } else if (row == ROWS - 1) {
            availableParts.removeIf(part -> part.getOut().equals(DIRECTION.BOTTOM));
        }
        if (col == 0) {
            availableParts.removeIf(part -> part.getOut().equals(DIRECTION.LEFT));
        } else if (col == COLUMNS - 1) {
            availableParts.removeIf(part -> part.getOut().equals(DIRECTION.RIGHT));
        }
    }

    private void removePartsBlockedByOtherParts(List<PART> availableParts, int row, int col) {
        if (row > 0 && track[row - 1][col] != null) {
            availableParts.removeIf(part -> part.getOut() == DIRECTION.TOP);
        }
        if (row < ROWS - 1 && track[row + 1][col] != null) {
            availableParts.removeIf(part -> part.getOut() == DIRECTION.BOTTOM);
        }
        if (col > 0 && track[row][col - 1] != null) {
            availableParts.removeIf(part -> part.getOut() == DIRECTION.LEFT);
        }
        if (col < COLUMNS - 1 && track[row][col + 1] != null) {
            availableParts.removeIf(part -> part.getOut() == DIRECTION.RIGHT);
        }
    }

    private int[] getNextPosition(int row, int col, DIRECTION direction) {
        return switch (direction) {
            case TOP -> new int[]{row - 1, col};
            case RIGHT -> new int[]{row, col + 1};
            case BOTTOM -> new int[]{row + 1, col};
            case LEFT -> new int[]{row, col - 1};
        };
    }

    private void generateWallsAndCheckpoints(int row, int col) {
        final double partSize = 1d / COLUMNS;

        double colOffset = row * partSize;
        double rowOffset = col * partSize;

        List<Boundary> boundaries = track[row][col].getBoundaries();
        for (Boundary boundary : boundaries) {
            walls.add(new Boundary(
                    (boundary.getA().x * partSize) + rowOffset, (boundary.getA().y * partSize) + colOffset,
                    (boundary.getB().x * partSize) + rowOffset, (boundary.getB().y * partSize) + colOffset
            ));
        }

        checkpoints.add(new Boundary(
                (track[row][col].getCheckpoint().getA().x * partSize) + rowOffset, (track[row][col].getCheckpoint().getA().y * partSize) + colOffset,
                (track[row][col].getCheckpoint().getB().x * partSize) + rowOffset, (track[row][col].getCheckpoint().getB().y * partSize) + colOffset
        ));

    }

    @Override
    public PVector getStartVelocity() {
        Boundary boundary = checkpoints.get(0);
        PVector v = PVector.sub(boundary.getA(), boundary.getB());
        v.rotate(-Math.PI * 0.5);
        v.normalize();
        return v;
    }

    public enum DIRECTION {
        LEFT, RIGHT, TOP, BOTTOM;

        public static DIRECTION getOpposite(DIRECTION direction) {
            return switch (direction) {
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
                case TOP -> BOTTOM;
                case BOTTOM -> TOP;
            };
        }

        public static DIRECTION getTurn(DIRECTION in, DIRECTION out) {
            if (DIRECTION.getLeftTurn(in) == out) return DIRECTION.LEFT;
            if (DIRECTION.getRightTurn(in) == out) return DIRECTION.RIGHT;
            return DIRECTION.TOP;
        }

        public static DIRECTION getRightTurn(DIRECTION direction) {
            return switch (direction) {
                case LEFT -> BOTTOM;
                case RIGHT -> TOP;
                case TOP -> LEFT;
                case BOTTOM -> RIGHT;
            };
        }

        public static DIRECTION getLeftTurn(DIRECTION direction) {
            return switch (direction) {
                case LEFT -> TOP;
                case RIGHT -> BOTTOM;
                case TOP -> RIGHT;
                case BOTTOM -> LEFT;
            };
        }
    }

    public enum PART {
        TOP_BOTTOM(DIRECTION.TOP, DIRECTION.BOTTOM, BOUNDARY_LEFT, BOUNDARY_RIGHT, CHECKPOINT_HORIZONTAL),
        TOP_LEFT(DIRECTION.TOP, DIRECTION.LEFT, BOUNDARY_TOP_RIGHT_BOTTOM_LEFT, CHECKPOINT_TOP_LEFT),
        TOP_RIGHT(DIRECTION.TOP, DIRECTION.RIGHT, BOUNDARY_TOP_LEFT_BOTTOM_RIGHT, CHECKPOINT_TOP_RIGHT),
        LEFT_TOP(DIRECTION.LEFT, DIRECTION.TOP, BOUNDARY_TOP_RIGHT_BOTTOM_LEFT, CHECKPOINT_TOP_LEFT),
        LEFT_RIGHT(DIRECTION.LEFT, DIRECTION.RIGHT, BOUNDARY_TOP, BOUNDARY_BOTTOM, CHECKPOINT_VERTICAL),
        LEFT_BOTTOM(DIRECTION.LEFT, DIRECTION.BOTTOM, BOUNDARY_TOP_LEFT_BOTTOM_RIGHT, CHECKPOINT_BOTTOM_LEFT),
        RIGHT_TOP(DIRECTION.RIGHT, DIRECTION.TOP, BOUNDARY_TOP_LEFT_BOTTOM_RIGHT, CHECKPOINT_TOP_RIGHT),
        RIGHT_LEFT(DIRECTION.RIGHT, DIRECTION.LEFT, BOUNDARY_TOP, BOUNDARY_BOTTOM, CHECKPOINT_VERTICAL),
        RIGHT_BOTTOM(DIRECTION.RIGHT, DIRECTION.BOTTOM, BOUNDARY_TOP_RIGHT_BOTTOM_LEFT, CHECKPOINT_BOTTOM_RIGHT),
        BOTTOM_TOP(DIRECTION.BOTTOM, DIRECTION.TOP, BOUNDARY_LEFT, BOUNDARY_RIGHT, CHECKPOINT_HORIZONTAL),
        BOTTOM_LEFT(DIRECTION.BOTTOM, DIRECTION.LEFT, BOUNDARY_TOP_LEFT_BOTTOM_RIGHT, CHECKPOINT_BOTTOM_LEFT),
        BOTTOM_RIGHT(DIRECTION.BOTTOM, DIRECTION.RIGHT, BOUNDARY_TOP_RIGHT_BOTTOM_LEFT, CHECKPOINT_BOTTOM_RIGHT);

        @Getter
        final List<Boundary> boundaries;

        @Getter
        final DIRECTION in;
        @Getter
        final DIRECTION out;

        @Getter
        final Boundary checkpoint;

        PART(DIRECTION in, DIRECTION out, Boundary... boundaries) {
            this.in = in;
            this.out = out;

            this.boundaries = new ArrayList<>();
            List<Boundary> bounds = Arrays.stream(boundaries).toList();
            for (int i = 0; i < bounds.size() - 1; i++) this.boundaries.add(bounds.get(i));

            this.checkpoint = bounds.get(bounds.size() - 1);
        }

        public DIRECTION getTurn() {
            return DIRECTION.getTurn(in, out);
        }

        public static List<PART> getPartsWithIn(DIRECTION direction) {
            return new ArrayList<>(Arrays.stream(values()).filter(part -> part.getIn() == direction).toList());
        }
    }
}
