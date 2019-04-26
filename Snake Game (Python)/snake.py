import pygame
import sys
import random

areaSize = 30
tileSize = 20
headColor = (0, 100, 0)
bodyColor = (0, 200, 0)
foodColor = (200, 0, 0)
gameSpeed = 10

class Snake():
    def __init__(self):
        self.head = [5, 5]
        self.body = [[self.head[0], self.head[1]], [5, 6], [5, 7], [6, 8]]
        self.direction = ""

    def changeDirTo(self, dir):
        if dir == "RIGHT" and not self.direction == "LEFT":
            self.direction = "RIGHT"
        if dir == "LEFT" and not self.direction == "RIGHT":
            self.direction = "LEFT"
        if dir == "UP" and not self.direction == "DOWN":
            self.direction = "UP"
        if dir == "DOWN" and not self.direction == "UP":
            self.direction = "DOWN"

    def move(self, foodPos):
        if self.direction == "RIGHT":
            self.head[0] += 1
        if self.direction == "LEFT":
            self.head[0] -= 1
        if self.direction == "UP":
            self.head[1] -= 1
        if self.direction == "DOWN":
            self.head[1] += 1

        self.body.insert(0, list(self.head))
        if self.head == foodPos:
            return 1
        else:
            self.body.pop()
            return 0

    def checkCollision(self):
        # Col with walls
        if self.head[0] > areaSize-1 or self.head[0] < 0:
            return True
        elif self.head[1] > areaSize-1 or self.head[1] < 0:
            return True
        # Col with self
        if len(snake.body) > 4:
            for body in self.body[1:]:
                if self.head == body:
                    return True
            return False

    def getBody(self):
        return self.body

    def canMove(self):
        return 0

class FoodSpawner():
    def __init__(self):
        self.head = [random.randrange(1, areaSize),random.randrange(1, areaSize)]
        self.isFoodOnScreen = True

    def spawnFood(self, snake):
        if not self.isFoodOnScreen:
            loop = True
            while loop:
                self.head = [random.randrange(1, areaSize), random.randrange(1, areaSize)]
                loop = False
                for i in enumerate(snake.body[0:]):
                    if self.head == i:
                        loop = True
            self.isFoodOnScreen = True
        return self.head

    def setFoodOnScreen(self, bool):
        self.isFoodOnScreen = bool


# init game
window = pygame.display.set_mode((areaSize*tileSize, areaSize*tileSize))
pygame.display.set_caption("Snake Game")
fps = pygame.time.Clock()

score = 0

snake = Snake()
foodSpawner = FoodSpawner()

canMove = 1

def gameStart():
    return 1

def gameOver():
    pygame.display.set_caption("SNAKE GAME | Score: " + str(score) + " | GAME OVER. Press any key to quit...")
    while True:
        event = pygame.event.wait()
        if event.type == pygame.KEYDOWN:
            break
    pygame.quit()
    sys.exit()

gameStart()

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            gameOver();
        elif event.type == pygame.KEYDOWN:
            if canMove:
                if event.key == pygame.K_RIGHT or event.key == pygame.K_d:
                    snake.changeDirTo('RIGHT')
                if event.key == pygame.K_LEFT or event.key == pygame.K_a:
                    snake.changeDirTo('LEFT')
                if event.key == pygame.K_UP or event.key == pygame.K_w:
                    snake.changeDirTo('UP')
                if event.key == pygame.K_DOWN or event.key == pygame.K_s:
                    snake.changeDirTo('DOWN')
                canMove = 0

    foodPos = foodSpawner.spawnFood(snake)
    if snake.move(foodPos) == 1:
        score += 1
        foodSpawner.setFoodOnScreen(False)

    window.fill(pygame.Color(225, 225, 225))
    # Draw snake
    head = 1
    for pos in snake.getBody():
        if head == 1:
            pygame.draw.rect(window, headColor, pygame.Rect(pos[0]*tileSize, pos[1]*tileSize, tileSize, tileSize))
            head = 0
        else:
            pygame.draw.rect(window, bodyColor, pygame.Rect(pos[0]*tileSize, pos[1]*tileSize, tileSize, tileSize))
    # Draw food
    pygame.draw.rect(window, foodColor, pygame.Rect(foodPos[0]*tileSize, foodPos[1]*tileSize, tileSize, tileSize))

    if(snake.checkCollision()):
        gameOver()

    pygame.display.set_caption("Snake Game | Score: " + str(score))
    pygame.display.flip()
    fps.tick(gameSpeed)
    canMove = 1
