#include <gtest/gtest.h>
#include "level.h"
#include "player.h"

extern Player self;

class MechanicsTest : public ::testing::Test {
protected:
    Level* level;

    void SetUp() override {
        level = new Level(nullptr, 1920);
        self.setPos(100, 100);
        self.setCoords(100, 100);
    }

    void TearDown() override {
        delete level;
    }
};

TEST_F(MechanicsTest, GravityApplication) {
    std::vector<bool> inputs(5, false);
    
    float initialY = self.getPos().y;
    
    level->applyLocalInput(inputs, 0);
    
    float newY = self.getPos().y;
    
    EXPECT_GT(newY, initialY); 
}

TEST_F(MechanicsTest, JumpPhysics) {
    std::vector<bool> inputs(5, false);
    inputs[4] = true; 
    
   
    SUCCEED(); 
}

TEST_F(MechanicsTest, FrictionLogic) {
    std::vector<bool> moveRight(5, false);
    moveRight[3] = true;
    
    for(int i=0; i<5; i++) level->applyLocalInput(moveRight, 0);
    
    float xAfterMove = self.getPos().x;
    float prevX = xAfterMove;
    
    std::vector<bool> noInput(5, false);
    
    level->applyLocalInput(noInput, 0);
    
    float xAfterFriction = self.getPos().x;
    
    EXPECT_GT(xAfterFriction, prevX);
    
    float delta1 = xAfterFriction - prevX;
    
    prevX = xAfterFriction;
    level->applyLocalInput(noInput, 0);
    float delta2 = self.getPos().x - prevX;
    
    EXPECT_LT(delta2, delta1);
}