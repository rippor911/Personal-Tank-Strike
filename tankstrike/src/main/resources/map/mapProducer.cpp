#include <iostream>
#include <vector>
#include <random>
#include <ctime>
#include <queue>
#include <utility>

class MazeGenerator {
private:
    int rows, cols;
    std::vector<std::vector<int>> maze;
    std::mt19937 rng;

    // 检查某个位置是否在迷宫范围内
    bool isValid(int x, int y) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    // 计算某个格子周围的障碍物数量
    int countSurroundingWalls(int x, int y) {
        int count = 0;
        int directions[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右
        
        for (int i = 0; i < 4; i++) {
            int nx = x + directions[i][0];
            int ny = y + directions[i][1];
            if (isValid(nx, ny) && maze[nx][ny] == 1) {
                count++;
            }
        }
        return count;
    }

    // 检查所有0是否连通
    bool isConnected() {
        // 找到第一个0作为起点
        int startX = -1, startY = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 0) {
                    startX = i;
                    startY = j;
                    break;
                }
            }
            if (startX != -1) break;
        }
        
        // 如果没有0，返回false
        if (startX == -1) return false;
        
        // 使用BFS检查连通性
        std::vector<std::vector<bool>> visited(rows, std::vector<bool>(cols, false));
        std::queue<std::pair<int, int>> q;
        q.push(std::make_pair(startX, startY));
        visited[startX][startY] = true;
        int connectedCount = 0;
        
        int directions[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        while (!q.empty()) {
            int x = q.front().first;
            int y = q.front().second;
            q.pop();
            connectedCount++;
            
            for (int i = 0; i < 4; i++) {
                int nx = x + directions[i][0];
                int ny = y + directions[i][1];
                if (isValid(nx, ny) && !visited[nx][ny] && maze[nx][ny] == 0) {
                    visited[nx][ny] = true;
                    q.push(std::make_pair(nx, ny));
                }
            }
        }
        
        // 计算总的0的数量
        int totalZeros = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 0) {
                    totalZeros++;
                }
            }
        }
        
        return connectedCount == totalZeros;
    }

    // 确保所有0连通的方法
    void ensureConnectivity() {
        // 如果已经连通，直接返回
        if (isConnected()) return;
        
        // 使用BFS找到所有连通区域
        std::vector<std::vector<bool>> visited(rows, std::vector<bool>(cols, false));
        std::vector<std::vector<std::pair<int, int>>> regions;
        
        int directions[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 0 && !visited[i][j]) {
                    std::vector<std::pair<int, int>> region;
                    std::queue<std::pair<int, int>> q;
                    q.push(std::make_pair(i, j));
                    visited[i][j] = true;
                    
                    while (!q.empty()) {
                        int x = q.front().first;
                        int y = q.front().second;
                        q.pop();
                        region.push_back(std::make_pair(x, y));
                        
                        for (int d = 0; d < 4; d++) {
                            int nx = x + directions[d][0];
                            int ny = y + directions[d][1];
                            if (isValid(nx, ny) && !visited[nx][ny] && maze[nx][ny] == 0) {
                                visited[nx][ny] = true;
                                q.push(std::make_pair(nx, ny));
                            }
                        }
                    }
                    
                    regions.push_back(region);
                }
            }
        }
        
        // 如果只有一个区域，已经连通
        if (regions.size() <= 1) return;
        
        // 连接所有区域
        for (unsigned int i = 1; i < regions.size(); i++) {
            // 随机选择两个区域中的点
            std::uniform_int_distribution<int> dist1(0, regions[0].size() - 1);
            std::uniform_int_distribution<int> dist2(0, regions[i].size() - 1);
            
            int idx1 = dist1(rng);
            int idx2 = dist2(rng);
            int x1 = regions[0][idx1].first;
            int y1 = regions[0][idx1].second;
            int x2 = regions[i][idx2].first;
            int y2 = regions[i][idx2].second;
            
            // 找到两点之间的路径并打通
            createPath(x1, y1, x2, y2);
            
            // 将当前区域合并到第一个区域
            regions[0].insert(regions[0].end(), regions[i].begin(), regions[i].end());
        }
    }
    
    // 创建两点之间的路径
    void createPath(int x1, int y1, int x2, int y2) {
        int x = x1, y = y1;
        
        while (x != x2 || y != y2) {
            // 随机选择移动方向（优先向目标方向移动）
            std::uniform_int_distribution<int> dirDist(0, 3);
            int direction = dirDist(rng);
            
            // 70%的概率向目标方向移动
            std::uniform_int_distribution<int> probDist(0, 9);
            if (probDist(rng) < 7) {
                if (x < x2) direction = 1; // 向下
                else if (x > x2) direction = 0; // 向上
                else if (y < y2) direction = 3; // 向右
                else if (y > y2) direction = 2; // 向左
            }
            
            int nx = x, ny = y;
            switch (direction) {
                case 0: nx--; break; // 上
                case 1: nx++; break; // 下
                case 2: ny--; break; // 左
                case 3: ny++; break; // 右
            }
            
            if (isValid(nx, ny)) {
                // 如果这个位置是墙，检查是否可以打通
                if (maze[nx][ny] == 1) {
                    // 检查打通后是否满足条件
                    maze[nx][ny] = 0;
                    if (countSurroundingWalls(nx, ny) > 3) {
                        maze[nx][ny] = 1; // 恢复
                    }
                }
                x = nx;
                y = ny;
            }
        }
    }

public:
    MazeGenerator(int r, int c, unsigned int seed) : rows(r), cols(c), rng(seed) {
        maze.resize(rows, std::vector<int>(cols, 0));
    }

    void generate() {
        std::uniform_int_distribution<int> dist(0, 1);
        
        // 初始随机生成迷宫
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = dist(rng);
            }
        }

        // 调整迷宫以满足条件
        bool changed;
        do {
            changed = false;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    int wallCount = countSurroundingWalls(i, j);
                    
                    // 如果某个格子的障碍物数量超过3，需要调整
                    if (wallCount > 3) {
                        // 随机选择减少障碍物的方法
                        std::uniform_int_distribution<int> actionDist(0, 1);
                        if (actionDist(rng) == 0) {
                            // 方法1：将该格子设为通路
                            maze[i][j] = 0;
                        } else {
                            // 方法2：随机移除周围的一个障碍物
                            std::vector<std::pair<int, int>> walls;
                            int directions[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                            
                            for (int d = 0; d < 4; d++) {
                                int ni = i + directions[d][0];
                                int nj = j + directions[d][1];
                                if (isValid(ni, nj) && maze[ni][nj] == 1) {
                                    walls.push_back(std::make_pair(ni, nj));
                                }
                            }
                            
                            if (!walls.empty()) {
                                std::uniform_int_distribution<int> wallDist(0, walls.size() - 1);
                                int idx = wallDist(rng);
                                int wx = walls[idx].first;
                                int wy = walls[idx].second;
                                maze[wx][wy] = 0;
                            }
                        }
                        changed = true;
                    }
                }
            }
        } while (changed); // 直到没有需要调整的格子
        
        // 确保所有0连通
        ensureConnectivity();
    }

    void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                std::cout << maze[i][j];
                if (j < cols - 1) std::cout << " ";
            }
            std::cout << std::endl;
        }
    }
};

int main() {

    freopen("map9.txt","w",stdout);

    int rows = 18;
    int cols = 24;
    
    // 使用当前时间作为随机数种子
    unsigned int seed = static_cast<unsigned int>(time(nullptr));
    
    MazeGenerator maze(rows, cols, seed);
    maze.generate();
    maze.print();

    // 如果需要自定义随机数种子，可以取消下面的注释
    /*
    unsigned int customSeed = 12345; // 自定义种子
    std::cout << "\n使用自定义种子 " << customSeed << " 生成的迷宫:" << std::endl;
    MazeGenerator customMaze(rows, cols, customSeed);
    customMaze.generate();
    customMaze.print();
    */

    return 0;
}