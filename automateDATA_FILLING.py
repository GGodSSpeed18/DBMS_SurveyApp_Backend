#  WARN: run the dbmsSetup.sql script first

import requests
from datetime import datetime, timedelta
import time
import random
from faker import Faker

ff = Faker()

login = "http://127.0.0.1:8080/auth/login"
logout = "http://127.0.0.1:8080/auth/logout"
signup = "http://127.0.0.1:8080/auth/signup"
forms = "http://127.0.0.1:8080/forms"
groups = "http://127.0.0.1:8080/groups"
users = "http://127.0.0.1:8080/users"
datatypes = "http://127.0.0.1:8080/datatypes"
roles = "http://127.0.0.1:8080/roles"
notifs = "http://127.0.0.1:8080/users/me/notifications"

token = ""

all_roles = []
all_users = []
all_datatypes = []
all_primitive_datatypes = []
all_groups = []
all_uses_options_datatypes = []
all_created_forms = []
authors_used = []
current_user = dict()
data_floats = []
data_integers = []
data_strings = []
list_of_responded_users = []
user1 = {
    "first_name": "gaurav", 
    "email": "gd@mail", 
    "gender": "M",
    "password": "123"
}

number_of_users = 15  # total user count
active_authors = 2  # total author count ( among users )
groups_per_author = 2
forms_per_author = 2
users_per_group = 3 # author is always in the group so other users added = (x-1)
# questions_per_form = 4  WARN: unused variable, have changed to a less generic way
forms_open_to_N_groups_of_author = 2    # should be smaller than groups created by authors
user_responses_per_form = 2     # should be less than forms_open_to * users_per_group
options_per_question_IF = 3

def create_all_users():
    for i in range(number_of_users):
        profile = {
            "first_name": ff.first_name()[:20],
            "last_name": ff.last_name()[:20],
            "dob": ff.date_of_birth().isoformat(),
            "email": ff.email()[:40],
            "gender": random.choice(['M', 'F', 'O']),
            "password": "123"
        }
        signup_user(profile)
        print(f"added user {i+1}")

def genHEADER():
    global token
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }
    return headers

def drop_user(profile):
    global token
    response = requests.delete(users+"/"+profile['user_id']+'/delete', headers=genHEADER())
    token = ""

def signup_user(profile):
    response = requests.post(signup, json=profile)
    if response.status_code != 200:
        print(f'FAILED creating user {response.status_code} - {response.text}')

def login_user(profile):
    global token
    global current_user
    response = requests.post(login, json={"email":profile['email'], "password": "123"})
    if response.status_code == 200:
        print(f"logged in {profile['email']}")
        token = response.json().get("token")
        current_user = requests.get(users+"/me", headers=genHEADER()).json()
        print(f"user_id : {current_user['user_id']}")
    else:
        print(f"FAILED to login: {response.status_code} - {response.text}")

def logout_user():
    global token
    response = requests.post(logout, headers=genHEADER())
    if response.status_code == 200:
        print("Logged out successfully -------------------------------------------------------------")
        token = "" 
    else:
        print(f"Failed to logout: {response.status_code} - {response.text}")
    time.sleep(2)

def create_groups(id):
    print(f'======= creating GROUPS for author : { id } =======')
    for i in range(groups_per_author):
        data = {
            "group_name": ff.word()
        }
        response = requests.post(groups, json=data, headers=genHEADER()) 
        if response.status_code == 200:
            print(f"created group {i+1}")
            print(f"name = { data['group_name'] }")
        else:
            print(f"failed to form group {i+1}")

def find_groups_by_author(id):
    return [d for d in all_groups if d['group_owner']==id]

def add_users_to_groups(id):
    global current_user
    groups_data = find_groups_by_author(id)
    for i in groups_data:
        if i['group_owner']==current_user['user_id']: #  TODO: Remove once right API added
            chosen_users = [ current_user['user_id'], user1['user_id'] ]
            if current_user['user_id'] == user1['user_id']:
                chosen_users.remove(user1['user_id'])
            print(f"########## Adding to group {i['group_id']} ######")
            for k in range(users_per_group-2):
                x = random.choice(all_users)
                while(x['user_id'] in chosen_users):
                    x = random.choice(all_users)
                chosen_users.append(x['user_id'])
            for k in chosen_users:
                response = requests.post(groups+'/'+str(i['group_id'])+"/adduser", headers=genHEADER(), params={"userid":k})
                if response.status_code == 200:
                    print(f"user added => {k}")
                else:
                    print(f"FAILED: {response.status_code} - {response.text}")

def choose_authors():
    global authors_used
    for i in range(active_authors):
        x = random.choice(all_users)
        while(x in authors_used):
            x = random.choice(all_users)
        login_user(x)
        authors_used.append(x)

        print(f"**** active author {i+1}")
        create_groups(current_user['user_id'])
        create_forms(current_user['user_id'])
        time.sleep(2)
        logout_user()

def open_forms_to_groups(id):
    # for i in authors_used:
    available_groups = find_groups_by_author(id)
    written_forms = requests.get(forms+"/user/me", headers=genHEADER()).json()
    for j in written_forms:
        chosen_groups = []
        print(f"==========  Opening form { j['form_id'] } =======")
        for k in range(forms_open_to_N_groups_of_author):
            x = random.choice(available_groups)
            while x in chosen_groups:
                x = random.choice(available_groups)
            chosen_groups.append(x)
            response = requests.post(forms+"/"+str(j['form_id'])+"/addgroup", params={"grpid":x['group_id']}, headers=genHEADER())
            if response.status_code == 200:
                print(f"added group => {x['group_id']} to form => {j['form_id']}")
            else:
                print(f"FAILED to add group: {response.status_code} - {response.text}")

def create_forms(id):
    global all_datatypes
    options_id = [d for d in all_datatypes if d['type_name'][:5]=='Radio']
    print(f'============ CREATING FORMS : AUTHOR { id } ============')
    for i in range(forms_per_author):
        form_object = {
            "form_name": ff.word(),
            "form_desc": ff.catch_phrase()[:150],
            "start_time": (datetime.now()-timedelta(hours=5, minutes=28)).isoformat(),
            "end_time": (datetime.now()+timedelta(4)).isoformat()
        }
        all_questions = []
        conditional_orderings = []
        for chosen_datatype in all_datatypes:
            question_def = {
                "data_type": chosen_datatype['type_id'],
                "required": ff.boolean(),
                "prompt": ff.text(max_nb_chars=100),
                "conditional": False
            }
            all_option_choices = []
            if chosen_datatype['usesOptions']:
                if chosen_datatype['type_id'] in data_floats:
                    for k in range(options_per_question_IF):
                        current_option = dict()
                        current_option['option_float'] = random.random()
                        all_option_choices.append(current_option)
                if chosen_datatype['type_id'] in data_integers:
                    for k in range(options_per_question_IF):
                        current_option = dict()
                        current_option['option_int'] = random.randint(-100000, 100000)
                        all_option_choices.append(current_option)
                if chosen_datatype['type_id'] in data_strings:
                    for k in range(options_per_question_IF):
                        current_option = dict()
                        current_option['option_string'] = ff.word()
                        all_option_choices.append(current_option)
            question_def['options'] = all_option_choices
            all_questions.append(question_def)

        # for j in range(questions_per_form):
        #     chosen_datatype = random.choice(all_datatypes)
        #     question_def = {
        #         "data_type": chosen_datatype['type_id'],
        #         "required": ff.boolean(),
        #         "prompt": ff.text(max_nb_chars=100),
        #         "conditional": False
        #     }
        #     #  BUG: Add functionality after the required API is created 
        #     #  if(question_def['datatype'] in options_id): 
        #     #  if(question_def['conditional'] == 1):
        #     all_option_choices = []
        #     if chosen_datatype['usesOptions']:
        #         if chosen_datatype['type_id'] in data_floats:
        #             for k in range(options_per_question_IF):
        #                 current_option = dict()
        #                 current_option['option_float'] = random.random()
        #                 all_option_choices.append(current_option)
        #
        #         if chosen_datatype['type_id'] in data_integers:
        #             for k in range(options_per_question_IF):
        #                 current_option = dict()
        #                 current_option['option_int'] = random.randint(-100000, 100000)
        #                 all_option_choices.append(current_option)
        #
        #         if chosen_datatype['type_id'] in data_strings:
        #             for k in range(options_per_question_IF):
        #                 current_option = dict()
        #                 current_option['option_string'] = ff.word()
        #                 all_option_choices.append(current_option)
        #     question_def['options'] = all_option_choices
        #     all_questions.append(question_def)
        default_ques_def = {
            "data_type": random.choice(all_primitive_datatypes), 
            "required": ff.boolean(),
            "prompt": "This should be a default Q !!",
            "conditional": False
        }
        alternate_ques_def = {
            "data_type": random.choice(all_primitive_datatypes), 
            "required": ff.boolean(),
            "prompt": "W0W alternate Q !!",
            "conditional": False
        }
        all_questions.append(default_ques_def)
        all_questions.append(alternate_ques_def)
        all_questions[4]['conditional'] = True
        #  BUG: conditional ordering is ambiguious in the current implementation
        new_condition = {
            "question_id": 5,
            "compare_value_int": all_questions[4]['options'][0]['option_int'],
            "question_default": 8,
            "question_alt": 9
        }
        conditional_orderings.append(new_condition)
        data = {
            "form": form_object,
            "questions": all_questions,
            "conditional_orderings": conditional_orderings
        }
        response = requests.post(forms+'/complete', headers=genHEADER(), json=data)
        if response.status_code == 200:
            print(f"form added => {i+1}")
        else:
            print(f"FAILED creating form: {response.status_code} - {response.text}")

def make_user_responses():
    print("================== USER RESPONSES ===================")
    all_data_required = []
    login_user(user1)
    for i in all_created_forms:
        current_data = dict()
        current_data['owner'] = requests.get(users+"/"+str(i['author']), headers=genHEADER()).json()
        current_data['form_id'] = i['form_id']
        current_data['questions_in_form'] = requests.get(forms+"/"+str(i['form_id'])+"/questions", headers=genHEADER()).json()
        current_data['open_to_users'] = requests.get(forms+"/"+str(i['form_id'])+"/users", headers=genHEADER()).json()
        all_data_required.append(current_data)
    time.sleep(2)
    logout_user()
    for r in all_data_required:
        questions_in_form = r['questions_in_form']
        open_to_users = r['open_to_users']
        if r['owner'] in open_to_users:
            open_to_users.remove(r['owner']);
        print(f'Extracted Info for { r["form_id"] }: Preparing responses ##################')
        random.shuffle(open_to_users)
        metadata = {
            "form_id": r['form_id'],
            "device": "localhost/script"
        }
        for u in open_to_users[:user_responses_per_form]:
            metadata['user_id'] = u['user_id']
            login_user(u)
            questions = []
            for q in questions_in_form:
                Q = {
                    "question_id": q['question_id'],
                }
                if q['data_type'] in all_uses_options_datatypes:
                    print(f"detected options setting values ~~~~~~ Q: { Q['question_id'] }, F: {metadata['form_id']}")
                    Q['response_int'] = random.randint(1, options_per_question_IF)
                elif q['data_type'] in data_floats:
                    Q["response_float"] = random.random()
                elif q['data_type'] in data_integers:
                    Q["response_int"] = random.randint(-1000, 1000)
                else:
                    Q["response_string"] = ff.sentence()[:60]
                questions.append(Q)
            data = {
                "metadata": metadata,
                "questions": questions
            }
            response = requests.post(users+"/respond/complete", headers=genHEADER(), json=data)
            if response.status_code == 200:
                print(f'Submitted response for form {r["form_id"]}')
                if u not in list_of_responded_users:
                    list_of_responded_users.append(u)
            else:
                print(f"FAILED to submit response {response.status_code} - {response.text}")
            time.sleep(2)
            logout_user()

def set_user_roles():
    global all_users, all_roles, list_of_responded_users, user1, authors_used
    print(f'>>>>>>>>>>>> Changing user roles : >>>>>')
    login_user(user1)
    for u in authors_used:
        if u['user_id']==user1['user_id']:
            continue
        response = requests.post(users+"/"+str(u['user_id'])+"/setrole", headers=genHEADER(), params={ "roleid": 5 })
        if response.status_code != 200:
            print(f'FAILED to assign authors: {response.status_code} - {response.text}')
            break
    for u in all_users:
        if u not in authors_used and u['user_id']!=user1['user_id']:
            selected_role = u['user_role']
            role_choices = [i for i in range(1, 7) if i != 2] #  NOTE: specific to the database entries
            role_choices_restr = [i for i in range(1, 7) if i!=2 and i!=6]
            if u in list_of_responded_users:
                selected_role = random.choice(role_choices_restr)
            else:
                selected_role = random.choice(role_choices)
            response = requests.post(users+"/"+str(u['user_id'])+"/setrole", headers=genHEADER(), params={"roleid": selected_role})
            if response.status_code != 200:
                print(f'FAILED to assign roles: {response.status_code} - {response.text}')
                break
    all_users = requests.get(users, headers=genHEADER()).json()
    print(">>>>> Role assignment complete : ")
    time.sleep(2)
    logout_user()

def main():
    global all_users, all_groups, all_datatypes, all_created_forms, user1, data_strings, data_integers, data_floats, all_roles, all_uses_options_datatypes, all_primitive_datatypes
    create_all_users()
    signup_user(user1)
    login_user(user1)
    user1 = requests.get(users+"/me", headers=genHEADER()).json()
    all_users = requests.get(users, headers=genHEADER()).json()
    all_datatypes = requests.get(datatypes, headers=genHEADER()).json()
    all_uses_options_datatypes = [ d['type_id'] for d in all_datatypes if d['usesOptions']==True ]
    all_primitive_datatypes = [ d['type_id'] for d in all_datatypes if d['usesOptions']==False ]
    all_roles = requests.get(roles, headers=genHEADER()).json()
    time.sleep(2)
    logout_user()

    data_floats = [d['type_id'] for d in all_datatypes if d['mappedToString']=='F']
    data_integers = [d['type_id'] for d in all_datatypes if d['mappedToString']=='I']
    data_strings = [d['type_id'] for d in all_datatypes if d['mappedToString']=='S']
    # print(data_floats, data_integers, data_strings, all_datatypes)
    choose_authors()

    login_user(user1)
    all_groups = requests.get(groups, headers=genHEADER()).json()
    time.sleep(2)
    logout_user()

    for i in authors_used:
        login_user(i)
        add_users_to_groups(current_user['user_id'])
        open_forms_to_groups(current_user['user_id'])
        time.sleep(2)
        logout_user()

    login_user(user1)
    all_created_forms = requests.get(forms, headers=genHEADER()).json()
    # my_notifs = requests.get(notifs, headers=genHEADER()).json()
    time.sleep(2)
    logout_user()
    time.sleep(2)
    make_user_responses()
    set_user_roles()
    # print(my_notifs)

if __name__ == "__main__":
    main()

#  HACK: (date, user_id) does not serve as a primary key
#  BUG: could not get an API for returning groups created by current user, without ROLE_MANAGE_GROUPS -> currently asigned default role ADMIN
#  BUG: typos in JWT auth
#  BUG: no API to add option values(i.e. insert into options table) currently only datatypes make QuesDTO to have has_options, and list of corresponding options
#  BUG: user should be able to delete himself, as of now only CAN_MANAGE_USERS has this permission
#  HACK: response to form does not check current user
#  BUG: No API for notifications, data_save, and general functionalities of analytic functions
