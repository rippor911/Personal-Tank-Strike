package com.util;

import java.util.Random;

public class MazeGenerator {

    private static final int WALL = 1;
    private static final int ROAD = 0;

    public static int[][] generateMap(int rows, int cols) {
        int h = (rows % 2 == 0) ? rows + 1 : rows;
        int w = (cols % 2 == 0) ? cols + 1 : cols;
        
        int[][] map = new int[h][w];

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                map[i][j] = WALL;
            }
        }

        Random rand = new Random();

        int roomCount = (w * h) / 100; 
        if (roomCount < 2) {
            roomCount = 2;
        }

        for (int i = 0; i < roomCount; i++) {
            int rh = rand.nextInt(2) * 2 + 3; // 3 or 5
            int rw = rand.nextInt(2) * 2 + 3; // 3 or 5
            
            int ry = rand.nextInt((h - rh) / 2) * 2 + 1;
            int rx = rand.nextInt((w - rw) / 2) * 2 + 1;

            for (int y = ry; y < ry + rh; y++) {
                for (int x = rx; x < rx + rw; x++) {
                    if (y < h - 1 && x < w - 1) {
                        map[y][x] = ROAD;
                    }
                }
            }
        }

        for (int y = 1; y < h; y += 2) {
            for (int x = 1; x < w; x += 2) {
                if (map[y][x] == WALL) {
                    carveMaze(map, x, y, rand);
                }
            }
        }

        connectRegions(map, h, w, rand);

        removeDeadEnds(map, (h * w) / 20);

        return map;
    }

    private static void carveMaze(int[][] map, int x, int y, Random rand) {
        map[y][x] = ROAD;

        int[][] dirs = {{0, -2}, {0, 2}, {-2, 0}, {2, 0}};
        shuffle(dirs, rand);

        for (int[] dir : dirs) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx > 0 && nx < map[0].length - 1 && ny > 0 && ny < map.length - 1) {
                if (map[ny][nx] == WALL) {
                    map[y + dir[1] / 2][x + dir[0] / 2] = ROAD;
                    carveMaze(map, nx, ny, rand);
                }
            }
        }
    }

    private static void connectRegions(int[][] map, int h, int w, Random rand) {
        for (int i = 0; i < (h * w); i++) {
            int y = rand.nextInt(h - 2) + 1;
            int x = rand.nextInt(w - 2) + 1;

            if (map[y][x] == WALL) {
                boolean connectVert = (map[y - 1][x] == ROAD && map[y + 1][x] == ROAD);
                boolean connectHorz = (map[y][x - 1] == ROAD && map[y][x + 1] == ROAD);
                
                if (connectVert || connectHorz) {
                    if (rand.nextInt(100) < 20) {
                        map[y][x] = ROAD;
                    }
                }
            }
        }
    }

    private static void removeDeadEnds(int[][] map, int count) {
        Random rand = new Random();
        int h = map.length;
        int w = map[0].length;
        
        for (int i = 0; i < count; i++) {
            int y = rand.nextInt(h - 2) + 1;
            int x = rand.nextInt(w - 2) + 1;
            if (map[y][x] == WALL) {
                map[y][x] = ROAD;
            }
        }
    }

    private static void shuffle(int[][] array, Random rand) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}