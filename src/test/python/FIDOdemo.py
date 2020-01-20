import requests
import json

BASE = 'http://localhost:8080'
BASE = 'http://localhost/atvapi'
#BASE = 'https://lightest-dev.iaik.tugraz.at/atvapi'
BASE = 'https://atvapi.tug.do.nlnetlabs.nl/atvapi'

TRANSACTION_FILE = '../testdata/fido1.json'


if 'result' not in requests.get(BASE + '/api/v3').json():
	print('Wrong BASE? 1')
	exit()
if requests.get(BASE + '/api/v3').json()['result'] != 1:
    print('Wrong BASE? 2')
    exit()


payload = json.loads(open(TRANSACTION_FILE, 'rb').read().decode('utf-8'))

print('###### QUERY: ######')
print(payload)

resp = requests.post(BASE + '/api/v3/verifyInstanceGD', json=payload)

print('')
print('')
print('###### RESPONSE: ######')
print(resp.text)

print('')
print('verificationResult: ' + resp.json()['verificationResult'])

if resp.json()['result'] == 1:
	print('               LoA: ' + resp.json()['loa'])


print('')
print('')
print('###### REPORT: ######')
for line in resp.json()['report']:
    print(line)
