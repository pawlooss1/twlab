from functools import reduce

wait_time = {0: [], 1: [], 2: [], 3: [], 4: []}


def add_wait_time(number, measurement):
    wait_time[number].append(measurement)


def wait_time_mean():
    return list(map(lambda t: (t[0], list_mean(t[1])), wait_time.items()))


def list_mean(values):
    return reduce(lambda a, b: a + b, values, 0) / len(values)


def print_measurements(measurements):
    for number, measurement in measurements:
        print('{} {}'.format(number, measurement))
