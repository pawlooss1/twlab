import threading
import time

import statistician
from asymmetric_fork import AsymmetricFork
from blocking_fork import BlockingFork
from hunger_fork import HungerFork
from waiter_fork import WaiterFork


class Philosopher(threading.Thread):

    def __init__(self, number, fork):
        super().__init__()
        self.number = number
        self.fork = fork

    def think(self):
        pass

    def eat(self):
        start = time.clock()
        self.fork.take(self.number)
        end = time.clock()
        statistician.add_wait_time(self.number, end - start)
        self.fork.put_back(self.number)

    def run(self):
        for _ in range(1000):
            self.think()
            self.eat()


if __name__ == '__main__':
    fork = AsymmetricFork()
    philosophers = list(map(lambda i: Philosopher(i, fork), range(5)))
    for philosopher in philosophers:
        philosopher.start()
    for philosopher in philosophers:
        philosopher.join()
    statistician.print_measurements(statistician.wait_time_mean())
