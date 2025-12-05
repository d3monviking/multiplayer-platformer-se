#include <gtest/gtest.h>
#include "../level.h" 
#include <SFML/Graphics.hpp>

class PhysicsTest : public ::testing::Test {
protected:
    Level* level;

    void SetUp() override {
        level = new Level(nullptr, 1920);
    }

    void TearDown() override {
        delete level;
    }
};

// Test AABB Collision Logic
TEST_F(PhysicsTest, DetectsIntersection) {
    sf::Sprite s1;
    s1.setPosition(100, 100);
    s1.setTextureRect(sf::IntRect(0, 0, 50, 50)); 

    sf::Sprite s2;
    s2.setPosition(120, 120);
    s2.setTextureRect(sf::IntRect(0, 0, 50, 50)); 

    // Expect collision (overlap)
    EXPECT_TRUE(level->colliding(s1, s2, s1.getPosition(), s2.getPosition()));
}

TEST_F(PhysicsTest, DetectsNoIntersection) {
    sf::Sprite s1;
    s1.setPosition(100, 100);
    s1.setTextureRect(sf::IntRect(0, 0, 50, 50));

    sf::Sprite s2;
    s2.setPosition(200, 200); // Far away
    s2.setTextureRect(sf::IntRect(0, 0, 50, 50));

    EXPECT_FALSE(level->colliding(s1, s2, s1.getPosition(), s2.getPosition()));
}

TEST_F(PhysicsTest, TimestampGeneration) {
    long long t1 = level->setCurrentTimestamp();
    // Simulate tiny delay
    long long t2 = level->setCurrentTimestamp();
    
    // Ensure time logic is monotonic or equal
    EXPECT_GE(t2, t1);
}