#include <gtest/gtest.h>
#include "player.h"
#include "tiles.h"

TEST(PowerUpTest, ApplyBoostLogic) {
    sf::Sprite s;
    PowerUp p(sf::Vector2f(0,0), 2.0f, 'P', s);
    
    float playerSpeed = 10.0f;
    
    p.applyBoost(playerSpeed);
    
    EXPECT_FLOAT_EQ(playerSpeed, 20.0f);
    EXPECT_TRUE(p.isBoostActive);
}

TEST(ShellTest, TypeCheck) {
    sf::Sprite s;
    Shell shell(sf::Vector2f(0,0), 'S', false, false, s);
    EXPECT_EQ(shell.getType(), 'S');
}


class PlayerTest : public ::testing::Test {
protected:
    Player* p;
    void SetUp() override {
        p = new Player(100.0f, 100.0f, 1);
    }
    void TearDown() override {
        delete p;
    }
};

TEST_F(PlayerTest, InitializationAndId) {
    EXPECT_EQ(p->get_id(), 1);
    EXPECT_FLOAT_EQ(p->getPos().x, 100.0f);
    EXPECT_FLOAT_EQ(p->getPos().y, 100.0f);
}

TEST_F(PlayerTest, SetPosition) {
    p->setPos(200.0f, 300.0f);
    EXPECT_FLOAT_EQ(p->getPos().x, 200.0f);
    EXPECT_FLOAT_EQ(p->getPos().y, 300.0f);
    
    EXPECT_FLOAT_EQ(p->sprite.getPosition().x, 200.0f);
}

TEST_F(PlayerTest, DimensionsCorrectForId) {
    sf::Vector2u dim = p->getDim();
    EXPECT_EQ(dim.x, 57);
    EXPECT_EQ(dim.y, 80);
}