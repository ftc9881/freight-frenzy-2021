adb connect 192.168.43.1:5555
for %%f in (*.json) do (
    adb -s 192.168.43.1:5555 push %%f /storage/emulated/0/Robot
)