import java.util.Random;

public class MazeGenerator {

    private static final int WALL = 1;
    private static final int ROAD = 0;

    public static int[][] generateMap(int rows, int cols) {
        // 保证行列是奇数，便于计算
        int h = (rows % 2 == 0) ? rows + 1 : rows;
        int w = (cols % 2 == 0) ? cols + 1 : cols;
        
        int[][] map = new int[h][w];

        // 1. 初始化全为墙
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                map[i][j] = WALL;
            }
        }

        Random rand = new Random();

        // 2. 随机挖出几个矩形房间 (Room Stamping)
        // 房间数量取决于地图大小
        int roomCount = (w * h) / 100; 
        if (roomCount < 2) {
            roomCount = 2;
        }

        for (int i = 0; i < roomCount; i++) {
            // 随机宽高 (奇数最好)
            int rh = rand.nextInt(2) * 2 + 3; // 3 or 5
            int rw = rand.nextInt(2) * 2 + 3; // 3 or 5
            
            // 随机位置 (保证奇数坐标)
            int ry = rand.nextInt((h - rh) / 2) * 2 + 1;
            int rx = rand.nextInt((w - rw) / 2) * 2 + 1;

            // 挖空
            for (int y = ry; y < ry + rh; y++) {
                for (int x = rx; x < rx + rw; x++) {
                    if (y < h - 1 && x < w - 1) {
                        map[y][x] = ROAD;
                    }
                }
            }
        }

        // 3. 在剩余空间生成迷宫 (使用递归回溯，但遇到已经空的地方就停止)
        for (int y = 1; y < h; y += 2) {
            for (int x = 1; x < w; x += 2) {
                // 如果这个点是墙，说明还没被房间占用，也没被迷宫访问，从这里开始生成
                if (map[y][x] == WALL) {
                    carveMaze(map, x, y, rand);
                }
            }
        }

        // 4. 连接孤立区域
        // 因为我们分别挖了房间和迷宫，它们之间可能被墙隔开了
        // 简单的做法是：遍历地图，找到能够连接两个不同区域的墙，打通它
        connectRegions(map, h, w, rand);

        // 5. 随机移除死胡同 (让地图更空旷，便于坦克移动)
        removeDeadEnds(map, (h * w) / 20);

        return map;
    }

    // 基础迷宫挖掘
    private static void carveMaze(int[][] map, int x, int y, Random rand) {
        map[y][x] = ROAD;

        int[][] dirs = {{0, -2}, {0, 2}, {-2, 0}, {2, 0}};
        shuffle(dirs, rand);

        for (int[] dir : dirs) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx > 0 && nx < map[0].length - 1 && ny > 0 && ny < map.length - 1) {
                if (map[ny][nx] == WALL) {
                    // 打通中间的墙
                    map[y + dir[1] / 2][x + dir[0] / 2] = ROAD;
                    carveMaze(map, nx, ny, rand);
                }
            }
        }
    }

    // 连接区域：非常关键的一步，防止有房间进不去
    private static void connectRegions(int[][] map, int h, int w, Random rand) {
        // 这个简易版逻辑：随机打通一些 墙壁，如果该墙壁两边都是空地
        // 更严谨的做法是用并查集(Union Find)，但对于游戏来说，随机打洞够用了
        for (int i = 0; i < (h * w); i++) { // 尝试多次
            int y = rand.nextInt(h - 2) + 1;
            int x = rand.nextInt(w - 2) + 1;

            if (map[y][x] == WALL) {
                // 如果上下都是路，或者左右都是路，打通它
                boolean connectVert = (map[y - 1][x] == ROAD && map[y + 1][x] == ROAD);
                boolean connectHorz = (map[y][x - 1] == ROAD && map[y][x + 1] == ROAD);
                
                if (connectVert || connectHorz) {
                    if (rand.nextInt(100) < 20) { // 20%概率打通，避免全通了
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
                // 检查周围只有一个墙邻居的情况太麻烦，
                // 直接随机把墙变成路，只要不是边界
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