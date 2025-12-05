#include <boost/asio.hpp>
#include <vector>
#include "../gamestructs.h"
#include "../player.h"


boost::asio::io_context io_context;
boost::asio::ip::udp::socket clientSocket(io_context);
boost::asio::ip::udp::endpoint serverEndpoint;

std::vector<SelfData> updates_buffer;
std::vector<std::vector<InterpolationData>> interpolation_buffer(4);


Player self(0, 0, 1);
std::vector<Player*> other_players;

int screen_width = 1920;
int screen_height = 1080;