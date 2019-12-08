import threading


class WaiterFork:
    def __init__(self):
        self.lock = threading.RLock()
        self.occupied = list(map(lambda _: False, range(5)))
        self.forks = list(map(lambda _: threading.Condition(lock=self.lock), range(5)))
        self.waiter = threading.Condition(lock=self.lock)
        self.how_many_present = 0

    def take(self, i):
        try:
            self.lock.acquire()
            while self.how_many_present == 4:
                self.waiter.wait()
            self.how_many_present += 1
            while self.occupied[i]:
                self.forks[i].wait()
            self.occupied[i] = True
            while self.occupied[(i + 1) % 5]:
                self.forks[(i + 1) % 5].wait()
            self.occupied[(i + 1) % 5] = True
        finally:
            self.lock.release()

    def put_back(self, i):
        try:
            self.lock.acquire()
            self.occupied[i] = False
            self.forks[i].notify()
            self.occupied[(i + 1) % 5] = False
            self.forks[(i + 1) % 5].notify()
            self.how_many_present -= 1
            self.waiter.notify()
        finally:
            self.lock.release()
