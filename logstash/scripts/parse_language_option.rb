# frozen_string_literal: true

TARGET_OPTION_INFO = 'target_option_info'
LANGUAGE_OPTION_NAME = 'lang_optnm'
FIELD_NAME = 'field_name'
OPTION_NAME = 'option_nm'
OPTION_CD_COLUMN = 'opt_cd'
OPTION_CD = 'option_cd'

def register(params)
  @field_name = params[FIELD_NAME]
end

def filter(event)

  result_options = []
  source_options = event.get(TARGET_OPTION_INFO)

  if source_options.nil? ||
     source_options.empty?
    return [event]
  end

  # puts source_options

  source_options.each do |source_option|

    if source_option.nil? ||
       source_option.empty? ||
       source_option[LANGUAGE_OPTION_NAME].nil? ||
       source_option[LANGUAGE_OPTION_NAME].empty?
      next
    end

    options = source_option[LANGUAGE_OPTION_NAME].split('|')

    # puts "options.length >>> #{options.length}\n"

    languages = {}
    option_object = {}

    option_object[OPTION_CD] = source_option[OPTION_CD_COLUMN]

    # puts "%s >>> %s\n" % ["option_object[OPTION_CD]", option_object[OPTION_CD]]

    options.each do |option|
      language_option = option.split(',')
      if language_option[0] == 'en'
        languages['srch'] = language_option[1]
        languages['en'] = language_option[1]
      else
        languages[(language_option[0]).to_s] = language_option[1]
      end
    end
    option_object[OPTION_NAME] = languages
    # puts "%s >>> %s\n" % ["option_object[OPTION_NAME][\"en\"]", option_object[OPTION_NAME]["en"]]

    result_options.push(option_object)
  end

  # print "result_options.size >>> #{result_options.size}\n"
  # printf "result_options >>> %s\n" % result_options
  # print "result_options.size >>> #{result_options.size}\n"

  # print "result_options.size >>> #{result_options.size}\n\n"
  # printf "result_options >>> %s\n" % result_options # 요소가 pop 된다.
  # printf "result_options >>> #{result_options.first[OPTION_NAME]}\n\n"
  # print "result_options.size >>> #{result_options.size}\n\n"

  event.set(@field_name, result_options)
  # puts "%s >>> %s\n\n" % ["event.get(@field_name)", event.get(@field_name)]
  # puts "%s >>> %s\n\n" % ["event.get(@field_name).first[\"option_nm\"]", event.get(@field_name).first["option_nm"]]
  # puts "%s >>> %s\n\n" % ["event.get(@field_name).first[\"option_nm\"].size", event.get(@field_name).first["option_nm"].size]

  [event]
end

=begin
docker run --rm \
  -v ${PWD}/scripts:/usr/share/logstash/scripts \
  docker.elastic.co/logstash/logstash:7.17.3 \
  logstash -e "filter { ruby { path => '/usr/share/logstash/scripts/parse_language_option.rb' } }" -t
=end
test 'with 3 languages' do
  parameters do
    {
      FIELD_NAME => OPTION_NAME
    }
  end

  in_event do
    {
      TARGET_OPTION_INFO => [
        {
          OPTION_CD_COLUMN => 'vehicle_option',
          LANGUAGE_OPTION_NAME =>
            'en,ABS (Anti-lock Brake System)|'\
            'ja,ABS（アンチロックブレーキシステム）|'\
            'ko,잠김방지 브레이크 시스템 (ABS)|'\
        }
      ]
    }
  end

  expect('return 4 results') do |events|
    # event = events.first
    events.first.get('option_nm').first['option_nm']['srch'].match(/ABS/) # 추가
    events.first.get('option_nm').first['option_nm']['en'].match(/ABS/)
    events.first.get('option_nm').first['option_nm']['en'].match(/ABS/)
    events.first.get('option_nm').first['option_nm']['ja'].match(/ABS/)
    events.first.get('option_nm').first['option_nm']['ko'].match(/ABS/)
  end
end
