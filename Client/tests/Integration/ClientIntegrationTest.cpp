#include <gtest/gtest.h>
#include <vector>
#include <thread>
#include <chrono>
#include "level.h"
#include "player.h"
#include "gamestructs.h"

extern std::vector<SelfData> updates_buffer;
extern std::vector<Move> move_history;
extern std::vector<std::vector<InterpolationData>> interpolation_buffer;
extern Player self;
extern std::vector<Player*> other_players;

class ClientIntegrationTest : public ::testing::Test {
protected:
    Level* level;

    void SetUp() override {
        level = new Level(nullptr, 1920);
        
        self.setPos(100.0f, 100.0f);
        self.setCoords(100.0f, 100.0f);
        self.setVel(0.0f, 0.0f);
        
        updates_buffer.clear();
        move_history.clear();
        for(auto& buf : interpolation_buffer) buf.clear();
        
        for(auto p : other_players) delete p;
        other_players.clear();
    }

    void TearDown() override {
        delete level;
    }
};

TEST_F(ClientIntegrationTest, ServerReconciliationLogic) {
    std::vector<bool> inputRight(5, false);
    inputRight[3] = true; 
    
    Move move1;
    move1.seq_num = 1;
    move1.thisMove = inputRight;
    move_history.push_back(move1);

    Move move2;
    move2.seq_num = 2;
    move2.thisMove = inputRight;
    move_history.push_back(move2);
    

    SelfData serverUpdate;
    serverUpdate.last_processed_seq_num = 1;
    serverUpdate.pos = sf::Vector2f(50.0f, 50.0f);
    serverUpdate.vel = sf::Vector2f(0.0f, 0.0f);
    
    updates_buffer.push_back(serverUpdate);
    
    level->processPendingUpdates();

    float finalX = self.getPos().x;
    EXPECT_GT(finalX, 49.0f); 
    EXPECT_LT(finalX, 100.0f); 
    EXPECT_TRUE(updates_buffer.empty());
}

TEST_F(ClientIntegrationTest, MultiplayerInterpolationFlow) {
    Player* p2 = new Player(0, 0, 2);
    other_players.push_back(p2);
    
    long long now = level->setCurrentTimestamp();
    
    InterpolationData dataA;
    dataA.timestamp = now - 150;
    dataA.pos = sf::Vector2f(100.0f, 100.0f);
    
    InterpolationData dataB;
    dataB.timestamp = now - 50;
    dataB.pos = sf::Vector2f(200.0f, 100.0f);
    
    interpolation_buffer[1].push_back(dataA);
    interpolation_buffer[1].push_back(dataB);
    
    level->InterpolateEntity(p2);
      
    sf::Vector2f result = p2->sprite.getPosition();
    
    EXPECT_GT(result.x, 99.0f); 
    EXPECT_LE(result.x, 200.0f);
    EXPECT_FLOAT_EQ(result.y, 100.0f);
}

TEST_F(ClientIntegrationTest, InputReplayOnCorrection) {
    Move m10, m11;
    m10.seq_num = 10; m10.thisMove = {false, false, false, true, false}; 
    m11.seq_num = 11; m11.thisMove = {false, false, false, true, false}; 
    
    move_history.push_back(m10);
    move_history.push_back(m11);
    
    SelfData serverState;
    serverState.last_processed_seq_num = 10;
    serverState.pos = sf::Vector2f(100.0f, 100.0f); 
    updates_buffer.push_back(serverState);
    
    level->processPendingUpdates();

    EXPECT_GT(self.getPos().x, 100.0f);
}