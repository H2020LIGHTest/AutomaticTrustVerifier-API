import requests
from base64 import b64encode

#BASE = 'http://localhost:8080' # idea
#BASE = 'http://localhost/atvapi' # docker
#BASE = 'https://lightest-dev.iaik.tugraz.at/atvapi' # lightest-dev
BASE = 'https://atvapi.tug.do.nlnetlabs.nl/atvapi/'


POLICY_FILE      = '../policies/policy_pades1.tpl'
TRANSACTION_FILE = '../testdata/document1.pdf'
#TRANSACTION_FILE = '../testdata/loi.pdf'


if requests.get(BASE + '/api/v1/').json()['result'] != 1:
    print('Wrong BASE?')


payload = dict()
payload['policy']      = b64encode(open(POLICY_FILE, 'rb').read()).decode('utf-8')
payload['transaction'] = b64encode(open(TRANSACTION_FILE, 'rb').read()).decode('utf-8')

# print(payload)

resp = requests.post(BASE + '/api/v1/addInstance', json=payload)

print(resp.text)


print('')
print('')
print('###### RESPONSE: ######')
print('verificationResult: ' + resp.json()['verificationResult'])
print('            result: ' + str(resp.json()['result']))


print('')
print('')
print('###### REPORT: ######')
for line in resp.json()['report']:
    print(line)
