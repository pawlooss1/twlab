import threading


class AsymmetricFork:
    def __init__(self):
        self.lock = threading.RLock()
        self.occupied = list(map(lambda _: False, range(5)))
        self.forks = list(map(lambda _: threading.Condition(lock=self.lock), range(5)))

    def take(self, i):
        try:
            self.lock.acquire()
            while self.occupied[first_fork(i)]:
                self.forks[first_fork(i)].wait()
            self.occupied[first_fork(i)] = True
            while self.occupied[second_fork(i)]:
                self.forks[second_fork(i)].wait()
            self.occupied[second_fork(i)] = True
        finally:
            self.lock.release()

    def put_back(self, i):
        try:
            self.lock.acquire()
            self.occupied[second_fork(i)] = False
            self.forks[second_fork(i)].notify()
            self.occupied[first_fork(i)] = False
            self.forks[first_fork(i)].notify()
        finally:
            self.lock.release()


def first_fork(i):
    if i % 2 == 0:
        return i
    else:
        return (i + 1) % 5


def second_fork(i):
    if i % 2 == 0:
        return (i + 1) % 5
    else:
        return i
