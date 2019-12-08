import threading


class HungerFork:
    def __init__(self):
        self.lock = threading.RLock()
        self.free = list(map(lambda _: 2, range(5)))
        self.philosophers = list(map(lambda _: threading.Condition(lock=self.lock), range(5)))

    def take(self, i):
        try:
            self.lock.acquire()
            while self.free[i] < 2:
                self.philosophers[i].wait()
            self.free[(i + 4) % 5] -= 1
            self.free[(i + 1) % 5] -= 1
        finally:
            self.lock.release()

    def put_back(self, i):
        try:
            self.lock.acquire()
            self.free[(i + 4) % 5] += 1
            self.free[(i + 1) % 5] += 1
            self.philosophers[(i + 4) % 5].notify()
            self.philosophers[(i + 1) % 5].notify()
        finally:
            self.lock.release()
