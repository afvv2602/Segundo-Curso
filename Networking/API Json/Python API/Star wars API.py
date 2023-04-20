import requests
def handler():
  # Api star wars
  # r = requests.get('https://swapi.dev/api/people')
  r = requests.get('http://jservice.io/')
  # Export the data for use in future steps
  return r.json()

def main():
    api = handler()
    

if __name__ == '__main__':
    main()