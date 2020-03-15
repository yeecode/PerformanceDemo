leftSteps=0 # 左侧起始步数
rightSteps=0 # 右侧起始步数
stepsLimit=10 # 总步数限制
stepsDifference=2 # 左右步数允许的最大差值

def leftForward():
    global leftSteps
    while True:
        print("left forward ... ...")
        while leftSteps < rightSteps + stepsDifference and leftSteps < stepsLimit:
            leftSteps = leftSteps + 1
            print("left steps: ",leftSteps)
        yield

def rightForward(c):
    global rightSteps
    while rightSteps < stepsLimit or leftSteps < stepsLimit:
        print("right forward ... ...")
        while rightSteps < leftSteps + stepsDifference and rightSteps < stepsLimit:
            rightSteps = rightSteps + 1
            print("right steps: ",rightSteps)
        c.send(None)
    c.close()

if __name__=='__main__':
    print("BEGIN")
    c = leftForward()
    rightForward(c)
    print("FINISHED")