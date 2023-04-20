import requests

def main():
    # showAll()
    # ShowAllComents()
    # ShowOneComent()
    # ShowOneUser()
    # todoList()
    posts()

def showAll():
    urlApi = 'https://jsonplaceholder.typicode.com/posts' 
    posts = requests.get(urlApi)
    posts = posts.json()
    for post in posts:
        print(post['id'])
        print(post['title'])
        print(post)
        print()

def ShowAllComents():
    urlApi = 'https://jsonplaceholder.typicode.com/posts/1/comments' 
    posts = requests.get(urlApi)
    posts = posts.json()
    print(posts)

def ShowOneComent():
    urlApi = 'https://jsonplaceholder.typicode.com/comments?postId=1' 
    posts = requests.get(urlApi)
    posts = posts.json()
    print(posts)

def ShowOneUser():
    urlApi ='https://jsonplaceholder.typicode.com/users/1' 
    posts = requests.get(urlApi)
    user = posts.json()
    name = user['name']
    street = user['address']['street']
    suite = user['address']['suite']
    city = user['address']['city']
    zipcode = user['address']['zipcode']
    phone = user['phone']
    print(f'Name: {name}')
    print(f'Street: {street}, {suite}, City: {city}, Zipcode: {zipcode}')
    print(f'Phone: {phone}')

def todoList():
    urlApi ='https://jsonplaceholder.typicode.com/users/1/todos?completed=false' 
    posts = requests.get(urlApi)
    todo_list = posts.json()
    print(todo_list)

def posts():
    urlApiPosts ='https://jsonplaceholder.typicode.com/posts' 
    posts = requests.get(urlApiPosts)
    posts = posts.json()
    for post in posts:
        post_id = post['id']
        post_title = post['title']
        post_body = post['body']
        print('\n')
        print(f'Post Title: {post_title}')
        print(f'Post Content: {post_body}')
        urlApiComments = f'https://jsonplaceholder.typicode.com/comments?postId={post_id}'
        comments = requests.get(urlApiComments)
        comments = comments.json()
        print('\n---Comments---\n')
        for comment in comments:
            comment_name = comment['name']
            comment_body = comment['body']
            print(f'Comment from: {comment_name}, message: {comment_body}')

if __name__ == "__main__":
    main()