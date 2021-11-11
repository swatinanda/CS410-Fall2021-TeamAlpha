import itertools
import os
from ast import literal_eval
import numpy as np
import pygraphviz as pgv

path = "."
fname = "mutual_infomation.txt"
topN = 10
arr = []
with open(os.path.join(path, fname)) as f:
    doc = f.readlines()
    for line in doc:
        try:
            tup = literal_eval(line)
            arr.append(tup)
        except:
            pass
A = np.array(arr)
print(A.shape)
G = pgv.AGraph(directed=False)

for i in range(A.shape[0]):
    print("{}->{}".format(i, A[i].argsort()[-topN:][::-1]))

    X = A[i].argsort()[-4:][::-1]
    for j in X:
        if round(float(A[i][j]), 2) > 0.2:
            G.add_edge(i, j, weight=10, color='green')
            G.get_edge(i, j).attr['label'] = round(float(A[i][j]), 2)
G.write('test.dot')

# create a png file
G.layout(prog='dot')  # use dot
G.draw('mi.png')



