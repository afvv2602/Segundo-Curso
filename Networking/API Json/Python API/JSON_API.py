import requests

user_id = ""

def main(): 
    user_id = input('Please enter your ID (1-10): ')
    print(f'\nHello {user_id}')
    op = int(input('Select what you want to check\n1.Personal Information\n2.Pending Tasks\n3.Posts\n'))
    match(op):
        case 1:
            personalInformation()
        case 2:
            todoList()
        case 3:
            posts()
            pass
    print('\nGoodbye!')

def personalInformation():
    urlApi =f'https://jsonplaceholder.typicode.com/users/{user_id}' 
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
    urlApi =f'https://jsonplaceholder.typicode.com/users/{user_id}/todos?completed=false' 
    posts = requests.get(urlApi)
    todo_list = posts.json()
    for todo in todo_list:
        id = todo['id']
        task = todo['title']
        print('\n')
        print(f'Task Number: {id}')
        print(f'Todo: {task}')

def posts():
    urlApiPosts =f'https://jsonplaceholder.typicode.com/posts/{user_id}'
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