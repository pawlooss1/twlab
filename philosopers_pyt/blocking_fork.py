import threading


class BlockingFork:
    def __init__(self):
        self.lock = threading.RLock()
        self.occupied = list(map(lambda _: False, range(5)))
        self.forks = list(map(lambda _: threading.Condition(lock=self.lock), range(5)))

    def take(self, i):
        try:
            self.lock.acquire()
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
        finally:
            self.lock.release()
